package com.example.demo.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.ScheduleJob;
import com.example.demo.utils.TaskUtils;

/**
* 
* @Description: 计划任务执行处 无状态
*/
public class QuartzJobFactory implements Job {
    public final Logger log = LoggerFactory.getLogger(QuartzJobFactory.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
    	ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
    	TaskUtils.invokMethod(scheduleJob);
    }
}