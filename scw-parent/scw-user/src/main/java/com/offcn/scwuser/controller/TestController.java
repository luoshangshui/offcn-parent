package com.offcn.scwuser.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/test")
    public String test1(){
        System.out.println("test my git");
        System.out.println("hello lucy");
        return "success";
    }
}
