package com.example.eurekaServer.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eurekaServer.service.HelloService;

@RestController
public class CustomerController {
	@Autowired
    HelloService service;
    
    @RequestMapping("/hello-ribbon")
    public String customer(){
        return service.helloService();
    }

}
