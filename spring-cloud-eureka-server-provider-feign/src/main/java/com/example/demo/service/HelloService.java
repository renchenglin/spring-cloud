package com.example.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("HELLO-SERVICE")
public interface HelloService {

	@RequestMapping("/hello")
	String hello();
}
