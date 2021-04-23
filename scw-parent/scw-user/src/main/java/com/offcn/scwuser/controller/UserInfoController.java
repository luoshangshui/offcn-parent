package com.offcn.scwuser.controller;

import com.offcn.dycommons.response.AppResponse;
import com.offcn.scwuser.pojo.TMemberAddress;
import com.offcn.scwuser.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "用户信息---查询用户地址")
@RestController
@RequestMapping("/user")
public class UserInfoController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    //查询地址
    @ApiOperation("获取用户地址")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户令牌",name = "accessToken",required = true)
    })
    @GetMapping("/findAddressList")
    public AppResponse<List<TMemberAddress>> findAddressList(String accessToken){
        String s = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(s)){
            return AppResponse.fail(null);
        }
        //获取地址
        int i = Integer.parseInt(s);
        List<TMemberAddress> address = userService.findAddress(i);
        return AppResponse.ok(address);
    }
}
