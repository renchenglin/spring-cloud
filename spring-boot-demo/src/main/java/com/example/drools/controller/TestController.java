package com.example.drools.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.drools.service.DroolsService;

/**
 * Created by Ares on 2018/6/23.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private DroolsService droolsService;

    @GetMapping("/showRults")
    public String showRults(){
        return droolsService.fireRule();
    }
    
    @GetMapping("/fireProductRule")
    public String fireProductRule(){
        return droolsService.fireProductRule();
    }
    
    @GetMapping("/testBoot")
    public String testBoot(){
        return "123456";
    }

}
