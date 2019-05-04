package com.example.demo.web;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("userController")
@Api(tags = "用户管理")
@RequestMapping("/api/userManagement/")
public class UserController {
	@Resource
	UserService UserService;
	
	@GetMapping("/{id}")
	@ApiOperation(value = "用户管理-查询用户-根据userName查询", code = 200)
    public User getUserByName(@NotNull  @PathVariable Long id) {
		User user = UserService.selectByPrimaryKey(id);
        return user;
    }
	
	@GetMapping("/usernum")
	@ApiOperation(value = "用户管理-查询用户-获取总用户数", code = 200)
    public Long getUserNum() {
		Long userNum = UserService.countByExample();
        return userNum;
    }
	
	@GetMapping("/address/{address}/{pageNum}/{pageSize}")
	@ApiOperation(value = "用户管理-查询用户-根据地址查询", code = 200)
    public List<User> getUserByAddress(@NotNull  @PathVariable String address,
    		@NotNull  @PathVariable int pageNum,
    		@NotNull  @PathVariable int pageSize,
    		@NotNull  @RequestParam boolean isLike) {
		List<User> users = UserService.selectByAddress(address, isLike, pageNum, pageSize);
        return users;
    }
//	@PostMapping("/findByPage")
//    public GeneralResponse findByPaging(Integer pageNum, Integer pageSize){
//        return UserService.findByPaging(pageNum,pageSize);
//    }

	
	/*
	 * 
POST:

> modify and update a resource
POST /questions/<existing_question> HTTP/1.1

> create a resource:
POST /questions HTTP/1.1

> Note that the following is an error:
POST /questions/<new_question> HTTP/1.1


PUT:

> To overwrite an existing resource:
PUT /questions/<existing_question> HTTP/1.1

> create a resource:
PUT /questions/<new_question> HTTP/1.1
	 */
	@PostMapping("/user")
    @ApiOperation(value = "用户管理-添加用户", code = 200)
    public User insert(@RequestBody @Validated User user) {
		UserService.insert(user);
        return user;
    }
	
	@PostMapping("/users")
    @ApiOperation(value = "用户管理-批量添加用户", code = 200)
    public boolean insertBatch(@RequestBody @Validated List<User> users) {
		UserService.insertBatch(users);
        return true;
    }
	
	@PostMapping("/user/{id}")
    @ApiOperation(value = "用户管理-添加用户", code = 200)
    public User update(@NotNull  @PathVariable Long id, @RequestBody @Validated User user) {
		user.setId(id);
		UserService.update(user);
		return user;
    }
	
	@DeleteMapping("/{id}")
    @ApiOperation(value = "用户管理-添加用户", code = 200)
    public Long delete(@NotNull  @PathVariable Long id) {
		UserService.delete(id);
		return id;
    }

}
