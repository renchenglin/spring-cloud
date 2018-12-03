package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.HelloService;


@RestController
public class FeignHelloController {
	@Autowired
	HelloService service;
    
    @RequestMapping("/hello-feign")
    public String customer(){
        return service.hello();
    }
}
