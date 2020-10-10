package com.ouyangwei.tinypermissiondemo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class ApiPermissionsUtil {

    private Map<String, Set<String>> permissionsMap = null;

    ApiPermissionsUtil(){
        permissionsMap = new HashMap<>();
        Set<String> set1 = new HashSet<>();
        set1.add("manage");
        permissionsMap.put("api_permission.dynamic_permission",set1);
    }

    public Set<String> getPermissions(String dynamic){
        return permissionsMap.get(dynamic);
    }

}
