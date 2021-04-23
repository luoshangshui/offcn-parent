package com.offcn.scworder.service.impl;

import com.offcn.dycommons.response.AppResponse;
import com.offcn.scworder.service.ProjectServiceFeign;
import com.offcn.scworder.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectInfoServiceFeignException implements ProjectServiceFeign {

    @Override
    public AppResponse<List<TReturn>> getReturnlist(Integer projectId) {
        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMsg("远程调用失败");
        return fail;
    }
}
