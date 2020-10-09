package com.ouyangwei.tinypermissiondemo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApiPermissionsUtil {

    private Map<String,String[]> permissionsMap = null;

    ApiPermissionsUtil(){
        permissionsMap = new HashMap<>();
        permissionsMap.put("api_permission.dynamic_permission",new String[]{"manage"});
    }

    public String[] getPermissions(String dynamic){
        return permissionsMap.get(dynamic);
    }

}
