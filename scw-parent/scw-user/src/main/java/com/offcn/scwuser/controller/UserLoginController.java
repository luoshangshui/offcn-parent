package com.offcn.scwuser.controller;

import com.offcn.dycommons.response.AppResponse;
import com.offcn.scwuser.component.SmsTemplate;
import com.offcn.scwuser.pojo.TMember;
import com.offcn.scwuser.service.UserService;
import com.offcn.scwuser.vo.req.UserRegistVo;
import com.offcn.scwuser.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Api(tags = "用户的模块--包含登录")
public class UserLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phoneNo", value = "手机号码", required = true)
    })
    @GetMapping("/sendCode")
    public AppResponse<Object> sendCode(String phoneNo) {
        //生验证码
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        stringRedisTemplate.opsForValue().set(phoneNo, code, 60 * 60 * 24 * 7, TimeUnit.SECONDS);
        //发送验证码
        Map<String, String> query = new HashMap<>();
        query.put("mobile", phoneNo);
        query.put("param", "code:" + code);
        query.put("tpl_id", "TP1711063");
        String sendCode = smsTemplate.sendCode(query);
        if (sendCode.equals("") || sendCode.equals("fail")) {
            return AppResponse.fail("发送失败");
        } else {
            return AppResponse.ok(sendCode);
        }
    }

    //注册
    @ApiOperation("用户注册")
    @PostMapping("/registe")
    public AppResponse<Object> register(UserRegistVo registVo) {
        TMember tMember = new TMember();
        //1,核验验证码是否正确
        String s = stringRedisTemplate.opsForValue().get(registVo.getLoginacct());
        if (s != null && s.equals(registVo.getCode())) {
            //2,registerVo填充到tmember
            BeanUtils.copyProperties(registVo, tMember);
            //3,注册数据库
            try {
                userService.registerUser(tMember);
                //4,删除验证码
                stringRedisTemplate.delete(registVo.getLoginacct());
                return AppResponse.ok("注册成功");
            } catch (Exception e) {
                e.printStackTrace();
                return AppResponse.fail(e.getMessage());
            }
        } else {
            return AppResponse.fail("验证码错误");
        }
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)
    })//@ApiImplicitParams：描述所有参数；@ApiImplicitParam描述某个参数
    public AppResponse<UserRespVo> login(String username, String password) {
        TMember tMember = userService.login(username, password);
        if (tMember == null) {
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名不正确");
            return fail;
        }
        String usertokken = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(usertokken, tMember.getId() + "", 20, TimeUnit.DAYS);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(tMember, userRespVo);
        userRespVo.setAccessToken(usertokken);
        return AppResponse.ok(userRespVo);
    }
}
