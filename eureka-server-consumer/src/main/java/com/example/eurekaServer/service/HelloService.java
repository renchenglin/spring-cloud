package com.example.eurekaServer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class HelloService {
    
    @Autowired
    RestTemplate restTemplate;            // 负载均衡ribbon对象
    
    // 熔断错误回调方法
    public String helloFallBack(){
        return "Error occurred!";
    }
    
    /**
     * 调用Eureka系统中名都为test-service的ribbon_service_a或ribbon_service_b的方法/hello
     * @return
     */
    // 注解指定发生错误时的回调方法
    @HystrixCommand(fallbackMethod="helloFallBack")
    public String helloService(){
        // Get请求调用服务，restTemplate被@LoadBalanced注解标记，Get方法会自动进行负载均衡
        // restTemplate会交替调用service_a和service_b
        return restTemplate.getForObject("http://HELLO-SERVICE/hello", String.class);
    }
}
