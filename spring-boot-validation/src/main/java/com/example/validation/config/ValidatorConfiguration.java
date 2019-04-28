package com.example.validation.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class ValidatorConfiguration {

	@Autowired
	ResourceBundleMessageSource messageSource;
    /**
     * Method:  开启快速返回
     * Description: 如果参数校验有异常，直接抛异常，不会进入到 controller，使用全局异常拦截进行拦截
     *
     * @param
     * @return org.springframework.validation.beanvalidation.MethodValidationPostProcessor
     */
	@Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    @Bean
    public Validator validator(){
    	/*ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
    			.configure()
    			.messageInterpolator(new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("i18n/messages_validate" )))
    			// 将fail_fast设置为true即可，如果想验证全部，则设置为false或者取消配置即可
    			.failFast( true )
    			.buildValidatorFactory();
    	Validator validator = validatorFactory.getValidator();
    	return validator;*/
    	LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
//    	方式一
    	try {
    		validator.setValidationMessageSource(messageSource);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	validator.setMessageInterpolator(new MessageInterpolator());
//    	方式二
    	return validator;
    }
    
    
    private class MessageInterpolator extends ResourceBundleMessageInterpolator {
    	@Override
    	public String interpolate(String message, Context context, Locale locale) {
    		// 获取注解类型
    		String annotationTypeName = context.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName();
    		
    		// 根据注解类型获取自定义的消息Code
    		String annotationDefaultMessageCode = VALIDATION_ANNATATION_DEFAULT_MESSAGES.get(annotationTypeName);
    		if (null != annotationDefaultMessageCode && !message.startsWith("javax.validation")
    				&& !message.startsWith("org.hibernate.validator.constraints")) {
    			// 如果注解上指定的message不是默认的javax.validation或者org.hibernate.validator等开头的情况，
    			// 则需要将自定义的消息Code拼装到原message的后面；
    			message += "{" + annotationDefaultMessageCode + "}";
    		}
    		
    		return super.interpolate(message, context, locale);
    	}
    }
    
    private static final Map<String, String> VALIDATION_ANNATATION_DEFAULT_MESSAGES =
    		new HashMap<String, String>(20) {{
    			put("CheckCase", "demo.validation.constraints.CheckCase.LOWER.message");
                put("NotNull", "demo.validation.constraints.NotNull.message");
    		}};

    
}