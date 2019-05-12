package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(columnDefinition = "varchar(128) comment '用户名'")
    private String userId;

    @Column(columnDefinition = "varchar(128) comment '姓名'")
    private String userName;
    
    @Column(columnDefinition = "int(11) comment '年龄'")
    private Integer age;
    
    @Column(columnDefinition = "varchar(128) comment '地址'")
    private String address;

    @Column(columnDefinition = "varchar(255) comment '居住地址'")
    private String homeAddress;

    @Column(columnDefinition = "varchar(50) comment '家庭电话'")
    private Long homeTel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Long getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(Long homeTel) {
		this.homeTel = homeTel;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
    
    

}
