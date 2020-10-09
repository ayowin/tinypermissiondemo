package com.ouyangwei.tinypermissiondemo.configs;

import com.ouyangwei.tinypermissiondemo.ApiPermissionsUtil;
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
import java.util.*;

@Aspect
@Component
public class TinyPermissionConfiguration {
    @Autowired
    UserUtil userUtil;
    @Autowired
    ApiPermissionsUtil apiPermissionsUtil;

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

        String value = tinyPermission.value();
//        System.out.println(value);
        Set<String> permissionSet = new HashSet<>();
        if(value != null && !value.equals("")){
            String[] permissions = value.split(",");
            if(permissions != null){
                for (String permission : permissions){
                    permissionSet.add(permission.trim());
                }
            }
        }

        String dynamic = tinyPermission.dynamic();
//        System.out.println(dynamic);
        if(dynamic != null && !value.equals("")){
            String[] permissions = apiPermissionsUtil.getPermissions(dynamic);
            if(permissions != null){
                for (String permission : permissions){
                    permissionSet.add(permission.trim());
                }
            }
        }

        Object[] args = joinPoint.getArgs();
        if(args != null && args.length > 0){
            /* 第一个参数是token */
            if(args[0] instanceof String){
                String token = (String)args[0];
                User user = userUtil.getUserByToken(token);
                if(user != null){
                    for (String permission : permissionSet){
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
