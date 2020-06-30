package com.kungyu.enums;

/**
 * @author wengyongcheng
 * @since 2020/6/30 11:17 上午
 */
public enum Format {

    INTEGER("integer",Integer.class,"int32"),
    LONG("integer",Integer.class,"int64"),
    FLOAT("float",Number.class,"float"),
    DOUBLE("double", Number.class, "double"),
    STRING("string", String.class, null),
    BYTE("byte", String.class, "byte"),
    BINARY("binary", String.class, "binary"),
    BOOLEAN("boolean", Boolean.class, null),
    DATE("date", String.class, "date"),
    DATETIME("dateTime", String.class, "date-time"),
    PASSWORD("password", String.class, "password");


    Format(String name,Class type,String format) {
        this.name = name;
        this.type = type;
        this.format = format;
    }

    private String name;

    private Class type;

    private String format;
}
