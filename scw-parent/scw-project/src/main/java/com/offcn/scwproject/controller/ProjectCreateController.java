package com.offcn.scwproject.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommons.enums.ProjectStatusEnume;
import com.offcn.dycommons.response.AppResponse;
import com.offcn.scwproject.contents.ProjectContents;
import com.offcn.scwproject.pojo.TReturn;
import com.offcn.scwproject.service.ProjectCreateService;
import com.offcn.scwproject.vo.req.ProjectBaseInfoVo;
import com.offcn.scwproject.vo.req.ProjectRedisStorageVo;
import com.offcn.scwproject.vo.req.ProjectReturnVo;
import com.offcn.vo.BaseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/project")
@Slf4j
@Api(tags = "项目的创建==初始化")
public class ProjectCreateController {
    @Autowired
    private ProjectCreateService projectCreateService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("项目初始化第一步")
    @PostMapping("/init")
    public AppResponse init(BaseVo vo) {
        String accessToken = vo.getAccessToken();
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (StringUtils.isEmpty(memberId)) {
            return AppResponse.fail("请登录");
        }
        //初始化项目
        String s = projectCreateService.initCreateProject(Integer.parseInt(memberId));
        return AppResponse.ok(s);
    }

    //项目保存
    @ApiOperation("第二步,保存页面基本信息")
    @PostMapping("/savebaseInfo")
    public AppResponse saveBaseInfo(ProjectBaseInfoVo vo) {
        String redis = redisTemplate.opsForValue().get(ProjectContents.TEMP_PROJECT_PREFIX + vo.getProjectToken());
        //字符串转换成对象类型
        ProjectRedisStorageVo vo1 = JSON.parseObject(redis, ProjectRedisStorageVo.class);
        //前端传来的vo存入redis中的vo
        BeanUtils.copyProperties(vo, vo1);
        //转换成字符串存入redis
        String s = JSON.toJSONString(vo1);
        redisTemplate.opsForValue().set(ProjectContents.TEMP_PROJECT_PREFIX + vo.getProjectToken(), s);
        return AppResponse.ok("ok");
    }

    //第三部保存回报
    @ApiOperation("项目初始化第三步---保存回报")
    @PostMapping("/savereturn")
    public AppResponse saveReturnInfo(@RequestBody List<ProjectReturnVo> pro) {
        //从集合拿到项目令牌
        String projectToken = pro.get(0).getProjectToken();
        //从redis取出临时对象
        String s = redisTemplate.opsForValue().get(ProjectContents.TEMP_PROJECT_PREFIX + projectToken);
        //字符串转对象
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(s, ProjectRedisStorageVo.class);
        //保存到集合里
        List list = new ArrayList();
        for (ProjectReturnVo projectReturnVo : pro) {
            TReturn tReturn = new TReturn();
            BeanUtils.copyProperties(projectReturnVo, tReturn);
            list.add(tReturn);
        }
        //把集合存入临时对象
        projectRedisStorageVo.setProjectReturns(list);
        //存入redis
        String s1 = JSON.toJSONString(projectRedisStorageVo);
        redisTemplate.opsForValue().set(ProjectContents.TEMP_PROJECT_PREFIX + projectToken, s1);
        return AppResponse.ok("回报成功");
    }

    //保存项目第四步
    @ApiOperation("保存项目第四步")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken",value = "用户令牌",required = true),
            @ApiImplicitParam(name = "projectToken",value="项目标识",required = true),
            @ApiImplicitParam(name="ops",value="用户操作类型 0-保存草稿 1-提交审核",required = true)})
    @PostMapping("/submit")
    public AppResponse saveProjectinfo(String accessToken, String projectToken, String ops) {
        String memberId = redisTemplate.opsForValue().get(accessToken);//用户令牌拿id判断是否登录
        if (StringUtils.isEmpty(memberId)) {
            return AppResponse.fail("请登录");
        }
        //获取项目
        String s = redisTemplate.opsForValue().get(ProjectContents.TEMP_PROJECT_PREFIX + projectToken);
        if(StringUtils.isEmpty(s)){
            return AppResponse.fail("没有该项目");
        }
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(s, ProjectRedisStorageVo.class);

        //保存项目
        if(ops.equals("1")){
            projectCreateService.saveProjectInfo(ProjectStatusEnume.SUBMIT_AUTH,projectRedisStorageVo);
            return AppResponse.ok("提交审核成功");
        } else if (ops.equals("0")) {
            projectCreateService.saveProjectInfo(ProjectStatusEnume.DRAFT,projectRedisStorageVo);
            return AppResponse.ok("存入草稿");
        } else {
            return AppResponse.fail("不允许的操作");
        }
    }
}
