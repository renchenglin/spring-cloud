package com.example.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "t_schedule_job")
public class ScheduleJob {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(columnDefinition = "varchar(150) comment '任务名称'")
    private String jobName;
    
    @Column(columnDefinition = "varchar(150) comment '任务分组'")
    private String jobGroup;
    
    @Column(columnDefinition = "varchar(150) comment 'cron表达式'")
    private String cronExpression;
    
    @Column(columnDefinition = "varchar(300) comment '任务class类名'")
    private String beanClass;
    
    @Column(columnDefinition = "varchar(150) comment '任务spring bean id'")
    private String springId;
    
    @Column(columnDefinition = "varchar(150) comment '任务bean执行方法名'")
    private String methodName;
    
    @Column(columnDefinition = "varchar(6) comment '任务状态：0-没启动;1-启动'")
    private String jobStatus;
    
    @Column(columnDefinition = "varchar(6) comment '是否等待上个任务完成:0-等待;1-不等待；'")
    private String isConcurrent;
    
    @Column(columnDefinition = "varchar(600) comment '任务描述'")
    private String description;
    
    @Column(columnDefinition = "timestamp comment '创建时间'")
    private Date createTime;
    
    @Column(columnDefinition = "timestamp comment '更新时间'")
    private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getBeanClass() {
		return beanClass;
	}

	public void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}

	public String getSpringId() {
		return springId;
	}

	public void setSpringId(String springId) {
		this.springId = springId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getIsConcurrent() {
		return isConcurrent;
	}

	public void setIsConcurrent(String isConcurrent) {
		this.isConcurrent = isConcurrent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	

    
}
