package com.example.demo.dynDatasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Order(-1)// 保证该AOP在@Transactional之前执行
@Component
public class DataSourceAop {

    @Pointcut("!@annotation(com.example.demo.dynDatasource.Master) " +
            "&& (execution(* com.example.demo.service..*.select*(..)) " +
            "|| execution(* com.example.demo.service..*.get*(..)))")
    public void readPointcut() {

    }

    @Pointcut("@annotation(com.example.demo.dynDatasource.Master) " +
            "|| execution(* com.example.demo.service..*.insert*(..)) " +
            "|| execution(* com.example.demo.service..*.add*(..)) " +
            "|| execution(* com.example.demo.service..*.update*(..)) " +
            "|| execution(* com.example.demo.service..*.edit*(..)) " +
            "|| execution(* com.example.demo.service..*.delete*(..)) " +
            "|| execution(* com.example.demo.service..*.remove*(..))")
    public void writePointcut() {

    }

    @Before("readPointcut()")
    public void read() {
        DBContextHolder.slave();
    }

    @Before("writePointcut()")
    public void write() {
        DBContextHolder.master();
    }


    /**
     * 另一种写法：if...else...  判断哪些需要读从数据库，其余的走主数据库
     */
//    @Before("execution(* com.example.demo.service.impl.*.*(..))")
//    public void before(JoinPoint jp) {
//        String methodName = jp.getSignature().getName();
//
//        if (StringUtils.startsWithAny(methodName, "get", "select", "find")) {
//            DBContextHolder.slave();
//        }else {
//            DBContextHolder.master();
//        }
//    }
}