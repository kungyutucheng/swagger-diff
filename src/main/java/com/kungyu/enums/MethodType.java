package com.kungyu.enums;

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
}
