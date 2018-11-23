package com.example.demo.rabbitmq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.rabbitmq.queue.CallBackSender;
import com.example.demo.rabbitmq.queue.HelloSender1;
import com.example.demo.rabbitmq.queue.HelloSender2;
import com.example.demo.rabbitmq.queue.UserSender;
import com.example.demo.rabbitmq.topic.FanoutSender;
import com.example.demo.rabbitmq.topic.TopicSender;

@RestController
@RequestMapping("/rabbit")
public class RabbitTest {
    
	@Autowired
    private HelloSender1 helloSender1;
	@Autowired
    private HelloSender2 helloSender2;
	@Autowired
    private UserSender userSender;
	@Autowired
    private TopicSender topicSender;
	@Autowired
    private FanoutSender fanoutSender;
	@Autowired
    private CallBackSender callBackSender;
	
	
    
    @PostMapping("/hello")
    public void hello() {
        helloSender1.send("hello1");
    }
    
    /**
     * 单生产者-多消费者
     */
    @PostMapping("/oneToMany")
    public void oneToMany() {
        for(int i=0;i<10;i++){
            helloSender1.send("hellomsg:"+i);
        }
        
    }
    
    /**
     * 多生产者-多消费者
     */
    @PostMapping("/manyToMany")
    public void manyToMany() {
        for(int i=0;i<10;i++){
            helloSender1.send("helloSender1:"+i);
            helloSender2.send("helloSender2:"+i);
        }
        
    }
    
    /**
     * 实体类传输测试
     */
    @PostMapping("/userTest")
    public void userTest() {
           userSender.send();
    }
    
    @PostMapping("/topicTest")
    public void topicTest() {
           topicSender.send();
    }
    
    /**
     * fanout exchange类型rabbitmq测试
     */
    @PostMapping("/fanoutTest")
    public void fanoutTest() {
           fanoutSender.send();
    }
    
    
    @PostMapping("/callback")
    public void callbak() {
        callBackSender.send();
    }
}