package com.ouyangwei.tinypermissiondemo.interfaces;

import java.util.Set;

public interface ConfigurationInterface {
    Set<String> getDynamicPermissions(String dynamic);
    Set<String> getUserPermissions(Object[] args);
}
