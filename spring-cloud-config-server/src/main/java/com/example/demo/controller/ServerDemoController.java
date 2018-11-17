package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ServerDemoController {
	@Value("${configServerKey}")
    String configServerKey;
	
    @RequestMapping(value = "/getServerConf")
    public String getServerConf(){
        return configServerKey;
    }
}
