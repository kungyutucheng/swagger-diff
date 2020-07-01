package com.kungyu.enums;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wengyongcheng
 * @since 2020/6/30 10:09 上午
 */
public enum MethodType {

    POST("post"),
    GET("get"),
    PUT("put"),
    PATCH("patch"),
    DELETE("delete"),
    HEADER("header");

    MethodType(String name) {
        this.name =name;
    }

    private String name;


    public String getName() {
        return name;
    }

    public static Optional<MethodType> getByName(String name) {
        return Arrays.stream(values()).filter(item -> StringUtils.equals(item.getName(), name)).findFirst();
    }
}
