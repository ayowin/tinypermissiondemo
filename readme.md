
# tinypermissiondemo

**微权限demo，自定义了@TinyPermission注解，使用该注解即可达到权限过滤的目的。**

* 示例代码
```java
@TinyPermission(value = "admin")
@RequestMapping(value = "/admin")
public String admin(@RequestHeader(name = "token")String token){
    return "admin...";
}

@TinyPermission(value = "admin,manage")
@RequestMapping(value = "/manage")
public String manage(@RequestHeader(name = "token")String token){
    return "manage...";
}

@TinyPermission(value = "admin,manage,custom")
@RequestMapping(value = "/custom")
public String custom(@RequestHeader(name = "token")String token){
    return "custom...";
}
```
以上代码可达到的效果为：
http请求头附带token参数，TinyPermission根据该token从mysql/redis/jwt等用户载体中取出用户，判断该用户是否具有value中众多权限的其中一个，如果用户有权限，则正常执行该请求，否则抛出对应的异常，**详细的代码，可阅读TinyPermissionConfiguration获悉。**

**注意：此demo并没有直接从任何数据载体中取出用户，而是以UserUtil来模拟该过程。**

* 有权限访问图

![image-20201009111015884](./readme_images/0.png)

* 无权限访问图

![image-20201009110717972](./readme_images/1.png)