package com.offcn.utils;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SmsUtil {
    @Value("${AppCode}")
    private String appcode;
    @Value("${tpl_id}")
    private String tpl_id;

    public HttpResponse sendSmsCode(String phone,String param) throws Exception {
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        querys.put("param", "code:"+param);
        querys.put("tpl_id", tpl_id);
        Map<String, String> bodys = new HashMap<String, String>();
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            return response;

    }
}
