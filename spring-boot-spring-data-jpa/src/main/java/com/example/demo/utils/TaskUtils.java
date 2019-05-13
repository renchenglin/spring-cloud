package com.example.demo.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.entity.ScheduleJob;

public class TaskUtils {
    public final static Logger log = LoggerFactory.getLogger(TaskUtils.class);

    /**
    * 通过反射调用scheduleJob中定义的方法
    * 
    * @param scheduleJob
    */
	public static void invokMethod(ScheduleJob scheduleJob) {
    	Object object = null;
    	Class<?> clazz = null;
    	if (StringUtils.isNotBlank(scheduleJob.getSpringId())) {
    		object = SpringUtils.getBean(scheduleJob.getSpringId());
    	} else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
    		try {
    			clazz = Class.forName(scheduleJob.getBeanClass());
    			object = clazz.newInstance();
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	
    	if (object == null) {
    		log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
    		return;
    	}
    	
    	clazz = object.getClass();
    	Method method = null;
    	try {
    		method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
    	} catch (NoSuchMethodException e) {
    		log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
    	} catch (SecurityException e) {
    		e.printStackTrace();
    	}
    	
    	if (method != null) {
    		try {
    			method.invoke(object);
    		} catch (IllegalAccessException e) {
    			e.printStackTrace();
    		} catch (IllegalArgumentException e) {
    			e.printStackTrace();
    		} catch (InvocationTargetException e) {
    			e.printStackTrace();
    		}
    	}
    	System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]----------启动成功");
    }
}
