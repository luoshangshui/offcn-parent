package com.offcn.scwproject.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.dycommons.enums.ProjectStatusEnume;
import com.offcn.scwproject.contents.ProjectContents;
import com.offcn.scwproject.enums.ProjectImageTypeEnume;
import com.offcn.scwproject.mapper.*;
import com.offcn.scwproject.pojo.TProject;
import com.offcn.scwproject.pojo.TProjectImages;
import com.offcn.scwproject.pojo.TProjectTag;
import com.offcn.scwproject.pojo.TReturn;
import com.offcn.scwproject.service.ProjectCreateService;
import com.offcn.scwproject.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private TProjectMapper projectMapper;
    @Autowired
    private TProjectImagesMapper projectImagesMapper;
    @Autowired
    private TProjectTagMapper projectTagMapper;
    @Autowired
    private TProjectTypeMapper projectTypeMapper;
    @Autowired
    private TReturnMapper returnMapper;

    @Override
    public String initCreateProject(Integer memberId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        //创建临时对象
        ProjectRedisStorageVo projectRedisStorageVo = new ProjectRedisStorageVo();
        projectRedisStorageVo.setMemberid(memberId);
        projectRedisStorageVo.setProjectToken(token);
        redisTemplate.opsForValue().set(ProjectContents.TEMP_PROJECT_PREFIX + token, JSON.toJSONString(projectRedisStorageVo));
        return token;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume statusEnume, ProjectRedisStorageVo project) {
        //保存项目
        TProject tProject = new TProject();
        BeanUtils.copyProperties(project, tProject);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String format = simpleDateFormat.format(new Date());
        tProject.setCreatedate(format);
        tProject.setDeploydate(format);
        tProject.setStatus(statusEnume.getCode() + "");
        projectMapper.insertSelective(tProject);
        //获取项目id
        Integer id = tProject.getId();
        //保存详情图片
        List<String> detailsImage = project.getDetailsImage();
        for (String s : detailsImage) {
            TProjectImages tProjectImages = new TProjectImages(null, id, s, ProjectImageTypeEnume.DETAILS.getCode());
            projectImagesMapper.insertSelective(tProjectImages);
        }
        //保存头图
        String headerImage = project.getHeaderImage();
        TProjectImages images = new TProjectImages(null,id,headerImage,ProjectImageTypeEnume.HEADER.getCode());
        projectImagesMapper.insertSelective(images);
        //保存标签
        List<Integer> tagids = project.getTagids();
        for (Integer tagid : tagids) {
            TProjectTag tProjectTag = new TProjectTag(null, id, tagid);
            projectTagMapper.insertSelective(tProjectTag);
        }
        //保存类型
        List<Integer> typeids = project.getTypeids();
        for (Integer typeid : typeids) {
            TProjectTag tProjectTag = new TProjectTag(null, id, typeid);
            projectTagMapper.insertSelective(tProjectTag);
        }
        //保存回报
        List<TReturn> projectReturns = project.getProjectReturns();
        for (TReturn projectReturn : projectReturns) {
            projectReturn.setProjectid(id);
            returnMapper.insertSelective(projectReturn);
        }
        //删除redis临时对象
        redisTemplate.delete(ProjectContents.TEMP_PROJECT_PREFIX+project.getProjectToken());
    }
}
