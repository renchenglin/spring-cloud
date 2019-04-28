package com.example.validation.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.example.validation.web.dto.User;
import com.example.validation.web.dto.User2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController("testController")
@Api(tags = "用户管理")
@RequestMapping("/api/user/")
@Validated //使用spring的validation
public class UserController {
	
	@GetMapping("/i18n")
    public String changeSessionLanauage(HttpServletRequest request, String lang){
        System.out.println(lang);
        if("zh_CN".equals(lang)){
            //代码中即可通过以下方法进行语言设置
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,new Locale("zh","CN"));
        }else if("en_US".equals(lang)){
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,new Locale("en","US"));
        }
        return "hello";
    }
	
	@GetMapping("/id/{userId}")
    @ApiOperation(value = "用户管理-查询用户-根据userId查询", code = 200)
    public User getUserByUserId(@PathVariable String userId) {
		
		User user = new User();
		user.setUserId(userId);
        return user;
    }
	/**
	 * PathVariable启用注解
	 * RequestParam启用注解
	 * @param name
	 * @param params
	 * @return
	 */
	@GetMapping("/name/{name}")
	@ApiOperation(value = "用户管理-查询用户-根据userName查询", code = 200)
    public User getUserByName(
                    @NotNull 
                    @Size(min = 1, max = 20, message = "用户名格式有误")
                    @PathVariable String name, 
                    
                    @NotNull 
                    @Size(min = 1, max = 20, message = "params用户名格式有误")
                    @RequestParam String params) {
        User user = new User();
        user.setUserId("userName," + params);
//        user.setName(name);
        return user;
    }
	
	@PostMapping("/save")
    @ApiOperation(value = "用户管理-添加用户", code = 200)
    public User save(@RequestBody @Valid User user) {
        return user;
    }
	
	@PostMapping("/save2")
    @ApiOperation(value = "用户管理-添加用户2", code = 200)
    public User2 save2(@RequestBody @Validated User2 user2) {
        return user2;
    }

}
