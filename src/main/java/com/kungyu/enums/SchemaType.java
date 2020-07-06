package com.kungyu.enums;

/**
 * @author wengyongcheng
 * @since 2020/7/6 3:56 下午
 */
public enum SchemaType {

    PARAMETER("入参"),
    RESPONSE("出参");

    private String type;


    SchemaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
