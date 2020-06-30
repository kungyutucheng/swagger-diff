package com.kungyu.enums;

/**
 * @author wengyongcheng
 * @since 2020/6/30 11:15 上午
 */
public enum InType {

    BODY("body"),
    OTHERS("others");

    InType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;
}
