package com.alen.netty.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;


/**
 * 测试
 *
 * @author alen
 * @create 2018-01-29 17:02
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger= LogManager.getLogger(TestController.class.getName());

    @RequestMapping("/test")
    public  String test(){
        return "success";
    }

    @RequestMapping("/log")
    public String log(){
        Method  method= null;
        try {
            method = this.getClass().getMethod("log");
            logger.info("方法名："+method.getName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
