package com.kungyu.model;

import java.util.Map;

/**
 * @author wengyongcheng
 * @since 2020/6/30 11:00 上午
 */
public class Response {

    private String description;

    private Schema schema;

    private Map<String, Header> headers;

    private Map<String, String> examples;
}
