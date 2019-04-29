package com.example.validation.post;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
/**
 * 仅仅对@Validated 注解的方法有效，待验证
 * @author Administrator
 *
 */
//@Component
public class ValidatedAnnotationBeanPostProcessor implements BeanPostProcessor {
	private static final Logger logger = LoggerFactory.getLogger(ValidatedAnnotationBeanPostProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!bean.getClass().isAnnotationPresent(Controller.class)
            && !bean.getClass().isAnnotationPresent(RestController.class)) {
            return bean;
        }

        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                if (!parameter.isAnnotationPresent(Validated.class)) {
                    continue;
                }

                Class<?> parameterType = parameter.getType();
                Field[] fields = parameterType.getDeclaredFields();
                for (Field field : fields) {
                    String fieldName = field.getName();

                    Annotation[] annotations = field.getDeclaredAnnotations();
                    for (Annotation annotation : annotations) {
                        String annotationName = annotation.annotationType().getName();
                        if (!annotationName.startsWith("javax.validation.constraints")
                                && !annotationName.startsWith("org.hibernate.validator.constraints")) {
                            // 如果不是JDK中的校验注解并且不是Hibernate中的校验注解，不需要处理
                            continue;
                        }

                        // 否则，如果注解存在message属性，并且未进行指定，则根据属性名称直接为注解指定message属性；
                        Field messageField;
                        try {
                            InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
                            Field memberValuesField = invocationHandler.getClass().getDeclaredField("memberValues");
                            if (null == memberValuesField) {
                                continue;
                            }

                            memberValuesField.setAccessible(true);

                            Map<String, String> map = (Map<String, String>) memberValuesField.get(invocationHandler);
                            String message = map.get("message");

                            // 如果message已经存在，并且是默认的消息，才进行替换，否则不替换
                            if (message.startsWith("{javax.validation")
                                    || message.startsWith("{org.hibernate.validator.constraints")) {
                                map.put("message", "{" + fieldName + "}");
                            }
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            logger.error("配置校验注解的Message属性失败！", e);

                            continue;
                        }
                    }
                }
            }
        }

        return bean;
    }

}
