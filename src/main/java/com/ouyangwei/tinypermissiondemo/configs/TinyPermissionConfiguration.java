package com.ouyangwei.tinypermissiondemo.configs;

import com.ouyangwei.tinypermissiondemo.UserUtil;
import com.ouyangwei.tinypermissiondemo.annotations.TinyPermission;
import com.ouyangwei.tinypermissiondemo.entities.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class TinyPermissionConfiguration {
    @Autowired
    UserUtil userUtil;

    @Pointcut("@annotation(com.ouyangwei.tinypermissiondemo.annotations.TinyPermission)")
    public void annotationPointcut(){

    }

    @Before("annotationPointcut()")
    public void before(JoinPoint joinPoint){
//        System.out.println("before...");
    }

    @Around("annotationPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
//        System.out.println("around...");

        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        TinyPermission tinyPermission = method.getAnnotation(TinyPermission.class);
//        System.out.println(tinyPermission.value());
        String[] permissions = tinyPermission.value().split(",");
        for (String permission : permissions){
            permission = permission.trim();
        }

        Object[] args = joinPoint.getArgs();
        if(args != null && args.length > 0){
            /* 第一个参数是token */
            if(args[0] instanceof String){
                String token = (String)args[0];
                User user = userUtil.getUserByToken(token);
                if(user != null){
                    for (String permission : permissions){
                        if(user.getRoles().contains(permission)){
                            return joinPoint.proceed();
                        }
                    }
                    throw new Exception("user is not permited");
                }
                throw new Exception("user not exist");
            }
        }
        throw new Exception("token not found");
    }

    @After("annotationPointcut()")
    public void after(JoinPoint joinPoint){
//        System.out.println("after...");
    }
}
