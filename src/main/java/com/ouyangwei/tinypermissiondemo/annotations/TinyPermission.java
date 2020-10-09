package com.ouyangwei.tinypermissiondemo.annotations;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TinyPermission {
    String value();
}
