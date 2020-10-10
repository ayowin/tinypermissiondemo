package com.ouyangwei.demo;

import com.ouyangwei.demo.entities.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserUtil {
    private Map<String, User> userMap = null;

    UserUtil(){
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("admin");
        user1.setPassword("123456");
        user1.setRoles("admin");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("ouyangwei");
        user2.setPassword("123456");
        user2.setRoles("admin,manage");

        User user3 = new User();
        user3.setId(3);
        user3.setUsername("zhangsan");
        user3.setPassword("123456");
        user3.setRoles("manage");

        User user4 = new User();
        user4.setId(4);
        user4.setUsername("lisi");
        user4.setPassword("123456");
        user4.setRoles("custom");

        userMap = new HashMap<>();
        userMap.put("token_admin",user1);
        userMap.put("token_ouyangwei",user2);
        userMap.put("token_zhangsan",user3);
        userMap.put("token_lisi",user4);
    }

    public User getUserByToken(String token){
        return userMap.get(token);
    }
}
