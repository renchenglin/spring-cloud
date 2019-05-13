package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

public class SchedulerConfiguration {

	
	@Bean
	public SchedulerFactoryBean  schedulerFactoryBean() {
	
		return new SchedulerFactoryBean();
	}

}
