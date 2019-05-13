package com.example.demo.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.ScheduleJob;
import com.example.demo.utils.TaskUtils;

public class QuartzJobFactoryDisallowConcurrentExecution implements Job {
	public final Logger log = LoggerFactory.getLogger(QuartzJobFactoryDisallowConcurrentExecution.class);
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		TaskUtils.invokMethod(scheduleJob);
	}
}
