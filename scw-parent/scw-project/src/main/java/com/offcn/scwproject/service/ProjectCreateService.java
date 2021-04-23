package com.offcn.scwproject.service;

import com.offcn.dycommons.enums.ProjectStatusEnume;
import com.offcn.scwproject.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {
    //初始化项目
    public String initCreateProject(Integer memberId);
    public void saveProjectInfo(ProjectStatusEnume statusEnume,ProjectRedisStorageVo project);
}
