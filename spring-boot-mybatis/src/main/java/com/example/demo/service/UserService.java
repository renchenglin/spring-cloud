package com.example.demo.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.model.UserExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
 
@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    
    public Long countByExample(){
    	UserExample userExample = new UserExample();
        return userMapper.countByExample(userExample);
    }
    
    public List<User> selectByAddress(String address, boolean isLike, int pageNum, int pageSize){
    	UserExample userExample = new UserExample();
    	if(isLike) {
    		userExample.createCriteria().andAddressLike("%" + address + "%");
    	} else {
    		userExample.createCriteria().andAddressEqualTo(address);
    	}
    	PageInfo<User> pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(() -> userMapper.selectByExample(userExample));
    	return pageInfo.getList();
    }
    
    public User selectByPrimaryKey(Long id){
        return userMapper.selectByPrimaryKey(id);
    }
    
//    public List<User> findByPaging(Integer pageNum, Integer pageSize) {
//    	UserExample example = new UserExample();
//        PageInfo pageInfo = PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(() -> userMapper.selectByExample(example));
//        return null;
//    }
    
    public void insert(User user){
        userMapper.insertSelective(user);
    }
    
    public void insertBatch(List<User> users){
    	for(User user : users) {
    		userMapper.insertSelective(user);
    	}
    }
    
    
    
    public void update(User user){
        userMapper.updateByPrimaryKeySelective(user);
    }
    
    public void delete(Long id) {
    	userMapper.deleteByPrimaryKey(id);
    }
}