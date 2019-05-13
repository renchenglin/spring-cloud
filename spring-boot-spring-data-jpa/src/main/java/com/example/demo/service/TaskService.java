package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ScheduleJob;
import com.example.demo.repository.ScheduleJobRepository;

@Service
@Transactional(rollbackOn=Exception.class)
public class TaskService {
	
	public static final String STATUS_RUNNING = "1";
	public static final String STATUS_NOT_RUNNING = "0";
	public static final String CONCURRENT_IS = "1";
	public static final String CONCURRENT_NOT = "0";
	 
	 
    private static Logger log = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private ScheduleJobRepository scheduleJobRepository;

    /**
     * 查询所有的定时任务
     */
    public List<ScheduleJob> getAllJobs() {
    	return scheduleJobRepository.findAll();
    }
     
    /**
    * 添加一个定时任务
    */
    public void addTask(ScheduleJob job) {
    	job.setCreateTime(new Date());
    	scheduleJobRepository.save(job);
    }
    
    /**
     * 删除一个job
     * 
     * @param scheduleJob
     * @throws SchedulerException
     */
     public void deleteJob(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
		scheduler.deleteJob(jobKey);
     }
     
     /**
      * 添加任务
      * 
      * @param scheduleJob
      * @throws SchedulerException
      */
      public void addJob(ScheduleJob job) throws SchedulerException {
    	  if (job == null || !STATUS_RUNNING.equals(job.getJobStatus())) {
    		  return;
    	  }

    	  Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	  log.debug(scheduler + "....................add..............");

    	  TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
    	  CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

    	  // 不存在，创建一个
    	  if (null == trigger) {
    		  Class<? extends Job> clazz = CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;
    		  JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();
    		  jobDetail.getJobDataMap().put("scheduleJob", job);
    		  CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
    		  trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
    		  scheduler.scheduleJob(jobDetail, trigger);
    	  } else {
    		  // Trigger已存在，那么更新相应的定时设置
    		  CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
    		  // 按新的cronExpression表达式重新构建trigger
    		  trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
    		  // 按新的trigger重新设置job执行
    		  scheduler.rescheduleJob(triggerKey, trigger);
    	  }
      }
      
      /**
       * 立即执行job
       * 
       * @param scheduleJob
       * @throws SchedulerException
       */
       public void runAJobNow(ScheduleJob scheduleJob) throws SchedulerException {
       	Scheduler scheduler = schedulerFactoryBean.getScheduler();
       	JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
       	scheduler.triggerJob(jobKey);
       }
      
      /**
       * 更新job时间表达式
       * 
       * @param scheduleJob
       * @throws SchedulerException
       */
      public void updateJobCron(ScheduleJob scheduleJob) throws SchedulerException {
    	  Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	  TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
    	  CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
    	  CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
    	  trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
    	  scheduler.rescheduleJob(triggerKey, trigger);
      }

       @PostConstruct
       public void init() throws Exception {
    	   //Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	   // 这里获取任务信息数据
    	   List<ScheduleJob> jobList = scheduleJobRepository.findAll();
    	   
    	   for (ScheduleJob job : jobList) {
    		   addJob(job);
    	   }
       }
    
    
      /**
       * 更改任务状态
       * 
       * @throws SchedulerException
       */
      public void changeStatus(Long jobId, String cmd) throws SchedulerException {
    	  ScheduleJob job = scheduleJobRepository.getOne(jobId);
    	  if (job == null) {
    		  return;
    	  }
    	  
    	  if ("stop".equals(cmd)) {
    		  deleteJob(job);
    		  job.setJobStatus(STATUS_NOT_RUNNING);
    	  } else if ("start".equals(cmd)) {
    		  job.setJobStatus(STATUS_RUNNING);
    		  addJob(job);
    	  }
    	  
    	  scheduleJobRepository.saveAndFlush(job);
      }
      
      /**
       * 更改任务 cron表达式
       * 
       * @throws SchedulerException
       */
       public void updateCron(Long jobId, String cron) throws SchedulerException {
    	   ScheduleJob job = scheduleJobRepository.getOne(jobId);
    	   if (job == null) {
    		   return;
    	   }

    	   job.setCronExpression(cron);
    	   if (STATUS_RUNNING.equals(job.getJobStatus())) {
    		   updateJobCron(job);
    	   }
    	   scheduleJobRepository.saveAndFlush(job);
       }
       
       
    
}