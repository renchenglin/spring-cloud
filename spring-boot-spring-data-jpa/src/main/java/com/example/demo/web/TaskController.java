package com.example.demo.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.quartz.CronScheduleBuilder;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.ScheduleJob;
import com.example.demo.service.TaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "任务管理")
@RequestMapping("/api/taskmanagement/")
public class TaskController {

    @Resource
    private TaskService taskService;

    /**
    * 查询所有的定时任务
    * @param request
    * @return
    */
    @GetMapping("/tasks")
    @ApiOperation(value = "任务管理-查询所有任务", code = 200)
    public List<ScheduleJob> taskList(HttpServletRequest request) {
    	List<ScheduleJob> taskList = taskService.getAllJobs();
    	return taskList;
    }


    /**
    * 添加一个定时任务
    * @param scheduleJob
    * @return retObj
    */
    @PostMapping("/tasks")
    @ApiOperation(value = "任务管理-添加任务", code = 200)
    @ResponseBody
    public ServerResult addTask(ScheduleJob scheduleJob) {
    	taskService.addTask(scheduleJob);
    	try {
			taskService.addJob(scheduleJob);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
    	
        return new ServerResult("添加成功","");
    }



    /**
    * 开启/关闭一个定时任务
    * @param request
    * @param jobId
    * @param cmd
    * @return
    */
    @PutMapping("/tasks/status/{id}/{cmd}")
    @ApiOperation(value = "任务管理-更新任务状态", code = 200)
    @ResponseBody
    public ServerResult changeJobStatus(@PathVariable Long id, @PathVariable String cmd) {
    	
    	try {
			taskService.changeStatus(id, cmd);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return new ServerResult("更新状态成功","");
    }



    /**
     * 修改定时任务的执行时间间隔
     * @param request
     * @param jobId
     * @param cron
     * @return
     */
    @PutMapping("/tasks/cron/{id}/{cron}")
    @ResponseBody
    public ServerResult updateCron(@PathVariable Long id, @PathVariable String cron) {
    	CronScheduleBuilder.cronSchedule(cron);
    	try {
			taskService.updateCron(id, cron);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return new ServerResult("更新状态成功","");
    }
}