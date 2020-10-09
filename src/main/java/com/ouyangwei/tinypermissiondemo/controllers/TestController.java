package com.ouyangwei.tinypermissiondemo.controllers;

import com.ouyangwei.tinypermissiondemo.annotations.TinyPermission;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping(value = "/")
    public String index(){
        return "index...";
    }

    @TinyPermission(value = "admin")
    @RequestMapping(value = "/admin")
    public String admin(@RequestHeader(name = "token")String token){
        return "admin...";
    }

    @TinyPermission(value = "admin,manage")
    @RequestMapping(value = "/manage")
    public String manage(@RequestHeader(name = "token")String token){
        return "manage...";
    }

    @TinyPermission(value = "admin,manage,custom")
    @RequestMapping(value = "/custom")
    public String custom(@RequestHeader(name = "token")String token){
        return "custom...";
    }
}
