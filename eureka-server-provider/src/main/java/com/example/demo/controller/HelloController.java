package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@RequestMapping(value="/hello", method=RequestMethod.GET)
	public String index() {
		logger.debug("------------------invoke index------------------");
		return "hello world";
	}

}
