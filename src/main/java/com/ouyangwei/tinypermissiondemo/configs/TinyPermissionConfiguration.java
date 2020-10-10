package com.ouyangwei.tinypermissiondemo.configs;

import com.ouyangwei.tinypermissiondemo.ApiPermissionsUtil;
import com.ouyangwei.tinypermissiondemo.UserUtil;
import com.ouyangwei.tinypermissiondemo.annotations.TinyPermission;
import com.ouyangwei.tinypermissiondemo.entities.User;
import com.ouyangwei.tinypermissiondemo.interfaces.ConfigurationInterface;
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

    private ConfigurationInterface configurationInterface = null;

    TinyPermissionConfiguration(){
        configurationInterface = new ConfigurationInterface() {
            @Override
            public Set<String> getDynamicPermissions(String dynamic) {
                Set<String> set = apiPermissionsUtil.getPermissions(dynamic);
                return set;
            }

            @Override
            public Set<String> getUserPermissions(Object[] args) {
                Set<String> set = new HashSet<>();
                if(args[0] instanceof String){
                    String token = (String)args[0];
                    User user = userUtil.getUserByToken(token);
                    String[] permissions = user.getRoles().split(",");
                    if(permissions != null){
                        for (String permission : permissions){
                            set.add(permission);
                        }
                    }
                }
                return set;
            }
        };
    }

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
            if(configurationInterface != null){
                Set<String> dynamicPermissionSet = configurationInterface.getDynamicPermissions(dynamic);
                if(dynamicPermissionSet != null){
                    for (String permission : dynamicPermissionSet){
                        permissionSet.add(permission.trim());
                    }
                }
            }
        }

        Object[] args = joinPoint.getArgs();
        if(args != null && args.length > 0){
            if(configurationInterface != null){
                Set<String> userPermissionSet = configurationInterface.getUserPermissions(args);
                if(userPermissionSet != null){
                    for (String permission : permissionSet){
                        if(userPermissionSet.contains(permission)){
                            return joinPoint.proceed();
                        }
                    }
                }
                throw new Exception("user is not permited or user not exist.");
            }
        }
        throw new Exception("unless one parameters for ConfigurationInterface to parse user's permissions.");
    }

    @After("annotationPointcut()")
    public void after(JoinPoint joinPoint){
//        System.out.println("after...");
    }
}
