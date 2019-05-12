package com.example.demo.web;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("userController")
@Api(tags = "用户管理")
@RequestMapping("/api/usermanagement/")
public class UserController {

    @Resource
    private UserRepository userRepository;
    
    @Resource
    private UserService userService;
    
    /**
     * 查询
     * @param user 用户信息
     * @return 添加成功后的用户id
     */
    @GetMapping("/users/search")
    @ApiOperation(value = "用户管理-查询用户-根据id查询", code = 200)
    public ServerResult search(User user, @RequestParam Integer page, 
    		@RequestParam Integer size, @RequestParam String orderField, @RequestParam String orderDirect){
    	Sort sort = null;
    	if(orderDirect == null) {
    		sort = new Sort(Direction.DESC, orderField);
    	}
    	if(orderDirect.equals("asc")) {
    		sort = new Sort(Direction.ASC, orderField);
    	} else {
    		sort = new Sort(Direction.DESC, orderField);
    	}
    	Page<User> pageUser = userService.find(user, page, size, sort);
    	
        return new ServerResult("查询成功", pageUser);
    }
    
    /**
     * 查询
     * @param user 用户信息
     * @return 添加成功后的用户id
     */
    @GetMapping("/users/searchanother")
    @ApiOperation(value = "用户管理-查询用户-根据id查询", code = 200)
    public ServerResult searchAnother(User user, @RequestParam Integer page, 
    		@RequestParam Integer size, @RequestParam String orderField, @RequestParam String orderDirect){
    	Sort sort = null;
    	if(orderDirect == null) {
    		sort = new Sort(Direction.DESC, orderField);
    	}
    	if(orderDirect.equals("asc")) {
    		sort = new Sort(Direction.ASC, orderField);
    	} else {
    		sort = new Sort(Direction.DESC, orderField);
    	}
    	Page<User> pageUser = userService.findAnother(user, page, size, sort);
    	
        return new ServerResult("查询成功", pageUser);
    }
    
    /**
     * 查询
     * @param user 用户信息
     * @return 添加成功后的用户id
     */
    @GetMapping("/users/searchtwo")
    @ApiOperation(value = "用户管理-查询用户-根据id查询", code = 200)
    public ServerResult searchTwo(@RequestParam String sql, @RequestParam Integer page, 
    		@RequestParam Integer size, @RequestParam String orderField, @RequestParam String orderDirect){
    	Sort sort = null;
    	if(orderDirect == null) {
    		sort = new Sort(Direction.DESC, orderField);
    	}
    	if(orderDirect.equals("asc")) {
    		sort = new Sort(Direction.ASC, orderField);
    	} else {
    		sort = new Sort(Direction.DESC, orderField);
    	}
    	Page<User> pageUser = userService.findTwo(sql, page, size, sort);
    	
        return new ServerResult("查询成功", pageUser);
    }

    /**
     * 添加用户
     * @param user 用户信息
     * @return 添加成功后的用户id
     */
    @PostMapping("/users")
    public ServerResult saveUser(@RequestBody User user){
        User saveUser = userRepository.save(user);
        return new ServerResult("添加成功",saveUser.getId());
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @param id 用户id
     * @return 更新结果
     */
    @PutMapping("/users/{id}")
    public ServerResult updateUserById(@RequestBody User user,@PathVariable Long id){
        user.setId(id);
        userRepository.save(user);
        return new ServerResult("修改成功",id);
    }

    /**
     * 获取用户信息
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/users/{id}")
    @ApiOperation(value = "用户管理-查询用户-根据id查询", code = 200)
    public ServerResult findUserById(@PathVariable Long id){
        User user = userRepository.getOne(id);
        return new ServerResult("查找成功",user);
    }

    /**
     * 删除用户信息
     * @param id 用户id
     * @return 删除结果
     */
    @DeleteMapping("/users/{id}")
    public ServerResult deleteUserById(@PathVariable Long id){
        userRepository.deleteById(id);
        return new ServerResult("删除成功",null);
    }

}