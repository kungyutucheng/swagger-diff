package com.kungyu.model;

import com.kungyu.enums.MethodType;

import java.util.Map;

/**
 * @author wengyongcheng
 * @since 2020/6/28 6:07 下午
 */
public class Path {

    private String path;

    private Map<MethodType, Method> methodList;

    private String ref;
}
