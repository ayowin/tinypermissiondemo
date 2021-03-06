package com.ouyangwei.tinypermission.annotations;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TinyPermission {
    String value();
    String dynamic() default "";
}
