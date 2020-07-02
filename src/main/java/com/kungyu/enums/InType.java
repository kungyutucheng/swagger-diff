package com.kungyu.enums;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author wengyongcheng
 * @since 2020/6/30 11:15 上午
 */
public enum InType {

    BODY("body"),
    OTHERS("others"),
    QUERY("query");

    InType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private String name;

    public static Optional<InType> getByName(String name) {
        return Arrays.stream(values()).filter(item -> StringUtils.equals(item.getName(), name)).findFirst();
    }
}
