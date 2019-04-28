package com.example.validation.web.advice;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
@ControllerAdvice
public class GlobalExceptionHandlerAdvice {
	@ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultInfo<?> validationErrorHandler(MethodArgumentNotValidException ex) {
        // 同样是获取BindingResult对象，然后获取其中的错误信息
        // 如果前面开启了fail_fast，事实上这里只会有一个信息
        //如果没有，则可能又多个
        List<String> errorInformation = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return new ResultInfo<>(400, errorInformation.toString(), null);
    }
	
	@ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ResultInfo<?> validationErrorHandler(ConstraintViolationException ex) {
	    List<String> errorInformation = ex.getConstraintViolations()
	            .stream()
	            .map(ConstraintViolation::getMessage)
	            .collect(Collectors.toList());
	    return new ResultInfo<>(400, errorInformation.toString(), null);
	}
}
