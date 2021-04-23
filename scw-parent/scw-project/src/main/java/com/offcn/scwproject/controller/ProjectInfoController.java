package com.offcn.scwproject.controller;

import com.offcn.dycommons.response.AppResponse;
import com.offcn.scwproject.pojo.*;
import com.offcn.scwproject.service.ProjectInfoService;
import com.offcn.scwproject.vo.resp.ProjectDetailVo;
import com.offcn.scwproject.vo.resp.ProjectVo;
import com.offcn.utils.OSSTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
@Api(tags = "项目模块--包含图片的上传")
@Slf4j
public class ProjectInfoController {
    @Autowired
    private OSSTemplate ossTemplate;
    @Autowired
    private ProjectInfoService projectInfoService;
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public AppResponse<Map<String,Object>> upload(@RequestParam("files") MultipartFile[] files) throws IOException {
        Map map = new HashMap<String,Object>();
        ArrayList<Object> list = new ArrayList<>();
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if(!file.isEmpty()){
                    String upload = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                    list.add(upload);
                }
            }
        }
        map.put("urls",list);
        log.debug("urls的地址是:{}=====集合是: ",list);
        return AppResponse.ok(map);
    }
    //获取回报
    @ApiOperation("获取项目的回报列表")
    @GetMapping("/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnlist(@PathVariable("projectId") Integer projectId){
        List<TReturn> returnList = projectInfoService.getReturnList(projectId);
        return AppResponse.ok(returnList);
    }
    //获取所有项目
    public AppResponse<List<ProjectVo>> findAllProject(){
        ArrayList<ProjectVo> list = new ArrayList<>();
        //查询所有项目
        List<TProject> projectList = projectInfoService.findProjectList();
        //遍历
        for (TProject tProject : projectList) {
            ProjectVo projectVo = new ProjectVo();
            BeanUtils.copyProperties(tProject,projectVo);
            //查询所有图片
            List<TProjectImages> projectImages = projectInfoService.findProjectImages(tProject.getId());
            for (TProjectImages projectImage : projectImages) {
                if (projectImage.getImgtype() == 0) {
                    projectVo.setHeaderImage(projectImage.getImgurl());
                }
            }
            list.add(projectVo);
        }
        return AppResponse.ok(list);
    }
    //获取项目详情
    @ApiOperation("获取项目详情")
    @GetMapping("/findProjectInfo/{projectId}")
    public AppResponse<ProjectDetailVo> findProjectInfo(@PathVariable("projectId")Integer projectId){
        TProject project = projectInfoService.findProjectInfo(projectId);
        ProjectDetailVo projectDetailVo = new ProjectDetailVo();
        BeanUtils.copyProperties(project,projectDetailVo);
        //设置图片
        List<TProjectImages> projectImages = projectInfoService.findProjectImages(projectId);
        ArrayList<String> arrayList = new ArrayList<>();
        for (TProjectImages projectImage : projectImages) {
            if (projectImage.getImgtype() == 0) {
                projectDetailVo.setHeaderImage(projectImage.getImgurl());
            } else {
                arrayList.add(projectImage.getImgurl());
            }
        }
        projectDetailVo.setDetailsImage(arrayList);
        //设置回报
        List<TReturn> returnList = projectInfoService.getReturnList(projectId);
        projectDetailVo.setProjectReturns(returnList);
        return AppResponse.ok(projectDetailVo);
    }
    //获取所有标签
    @ApiOperation("获取所有标签")
    @GetMapping("/findAllTag")
    public AppResponse<List<TTag>> findAllTag(){
        return AppResponse.ok(projectInfoService.findAllTag());
    }
    //获取所有类型
    @ApiOperation("获取所有类型")
    @GetMapping("/findAllType")
    public AppResponse<List<TType>> findAllType(){
        return AppResponse.ok(projectInfoService.findAllType());
    }
    //获取回报信息
    @ApiOperation("获取回报信息")
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> findReturnInfo(@PathVariable("returnId") Integer returnId){
        return AppResponse.ok(projectInfoService.findReturnInfo(returnId));
    }
}
