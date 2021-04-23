package com.offcn.scwproject.service.impl;

import com.offcn.scwproject.mapper.*;
import com.offcn.scwproject.pojo.*;
import com.offcn.scwproject.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {
    @Autowired
    private TReturnMapper returnMapper;
    @Autowired
    private TProjectMapper projectMapper;
    @Autowired
    private TProjectImagesMapper projectImagesMapper;
    @Autowired
    private TTagMapper tagMapper;
    @Autowired
    private TTypeMapper typeMapper;
    @Override
    public List<TReturn> getReturnList(Integer projectId) {
        //获取回报
        TReturnExample example = new TReturnExample();
        example.createCriteria().andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(example);

    }

    @Override
    public List<TProject> findProjectList() {
        return projectMapper.selectByExample(null);
    }

    @Override
    public List<TProjectImages> findProjectImages(Integer projectId) {
        TProjectImagesExample e = new TProjectImagesExample();
        e.createCriteria().andProjectidEqualTo(projectId);
        return projectImagesMapper.selectByExample(e);
    }

    @Override
    public TProject findProjectByProjectId(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public TProject findProjectInfo(Integer projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public List<TTag> findAllTag() {
        return tagMapper.selectByExample(null);
    }

    @Override
    public List<TType> findAllType() {
        return typeMapper.selectByExample(null);
    }

    @Override
    public TReturn findReturnInfo(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }
}
