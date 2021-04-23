package com.offcn.scworder.service;

import com.offcn.dycommons.response.AppResponse;
import com.offcn.scworder.service.impl.ProjectInfoServiceFeignException;
import com.offcn.scworder.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCW-PROJECT",fallback = ProjectInfoServiceFeignException.class)
public interface ProjectServiceFeign {
    @GetMapping("/project/details/returns/{projectId}")
    public AppResponse<List<TReturn>> getReturnlist(@PathVariable("projectId") Integer projectId);
}
