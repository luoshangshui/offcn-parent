package com.offcn.scwproject.service;

import com.offcn.scwproject.pojo.*;

import java.util.List;

public interface ProjectInfoService {
    public List<TReturn> getReturnList(Integer projectId);
    //查询所有项目
    List<TProject> findProjectList();
    //查询项目图片
    List<TProjectImages> findProjectImages(Integer projectId);
    //查询项目详情
    TProject findProjectByProjectId(Integer projectId);
    //获取项目详情
    TProject findProjectInfo(Integer projectId);
    //查询所有标签
    List<TTag> findAllTag();
    //查询所有类型
    List<TType> findAllType();
    //查询所有回报
    TReturn findReturnInfo(Integer returnId);
}
