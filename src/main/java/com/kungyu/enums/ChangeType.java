package com.kungyu.enums;

/**
 * @author wengyongcheng
 * @since 2020/7/2 6:29 下午
 */
public enum ChangeType {
    API_ADD("新增接口"),
    API_DELETE("删除接口"),
    API_DESC_MODIFY("接口描述修改"),
    API_CONSUMES_MODIFY("接口接收参数格式修改"),
    API_PRODUCES_MODIFY("接口响应参数格式修改"),
    API_SUMMARY_MODIFY("接口描述参数修改"),
    API_TAGS_MODIFY("接口标签修改"),

    HTTP_CODE_ADD("新增http状态码"),
    HTTP_CODE_DELETE("删除http状态码"),

    METHOD_ADD("新增method"),
    METHOD_DELETE("删除method"),

    INPUT_PARAMETER_ADD("新增入参"),
    INPUT_PARAMETER_DELETE("删除入参"),
    INPUT_PARAMETER_TYPE_MODIFY("入参类型修改"),
    INPUT_PARAMETER_DESC_MODIFY("入参描述修改"),

    OUTPUT_PARAMETER_ADD("新增出参"),
    OUTPUT_PARAMETER_DELETE("删除出参"),
    OUTPUT_PARAMETER_NAME_MODIFY("出参名称修改"),
    SCHEMA_TYPE_MODIFY("出参类型修改"),
    SCHEMA_TITLE_MODIFY("出参标题修改"),
    SCHEMA_FORMAT_MODIFY("出参格式修改"),
    SCHEMA_DESC_MODIFY("出参描述修改"),
    SCHEMA_DEFAULT_VALUE_MODIFY("出参默认值修改"),
    SCHEMA_MULTIPLE_OF_MODIFY("出参multipleOf修改"),
    SCHEMA_MAXIMUM_MODIFY("出参最大值修改"),
    SCHEMA_EXCLUSIVE_MAXIMUM_MODIFY("出参不含最大值修改"),
    SCHEMA_MINIMUM_MODIFY("出参最小值修改"),
    SCHEMA_EXCLUSIVE_MINIMUM_MODIFY("出参不含最小值修改"),
    SCHEMA_MAX_LENGTH_MODIFY("出参最大长度修改"),
    SCHEMA_MIN_LENGTH_MODIFY("出参最小长度修改"),
    SCHEMA_PATTERN_MODIFY("出参模式修改"),
    SCHEMA_MAX_ITEMS_MODIFY("出参最大个数修改"),
    SCHEMA_MIN_ITEMS_MODIFY("出参最小个数修改"),
    SCHEMA_UNIQUE_ITEMS_MODIFY("出参数组唯一值修改"),
    SCHEMA_MAX_PROPERTIES_MODIFY("出参对象属性个数最大值修改"),
    SCHEMA_MIN_PROPERTIES_MODIFY("出参对象属性个数最小值修改"),
    SCHEMA_REQUIRED_MODIFY("出参必填属性修改"),
    SCHEMA_ENUM_VALUES_MODIFY("出参可选值列表修改"),
    SCHEMA_READONLY_MODIFY("出参只读修改"),
    SCHEMA_DISCRIMINATOR_MODIFY("出参判别器修改"),
    SCHEMA_EXAMPLE_MODIFY("出参例子修改"),
    SCHEMA_EXTERNAL_DOCUMENTATION_DESC_MODIFY("出参外部文档描述修改"),
    SCHEMA_EXTERNAL_DOCUMENTATION_URL_MODIFY("出参外部文档链接修改"),

    OUTPUT_OBJECT_DESC_MODIFY("出参对象描述修改"),

    OUTPUT_HEADER_ADD("新增出参header"),
    OUTPUT_HEADER_DELETE("删除出参header"),
    OUTPUT_HEADER_DESC_MODIFY("出参header描述修改"),
    OUTPUT_HEADER_TYPE_MODIFY("出参header字段类型修改"),
    OUTPUT_HEADER_FORMAT_MODIFY("出参header字段格式修改"),
    OUTPUT_HEADER_COLLECTION_FORMAT_MODIFY("出参header集合格式修改"),
    OUTPUT_HEADER_DEFAULT_VALUE_MODIFY("出参header默认值修改"),
    OUTPUT_HEADER_MAXIMUM_MODIFY("出参header最大值修改"),
    OUTPUT_HEADER_EXCLUSIVE_MAXIMUM_MODIFY("出参header不含最大值修改"),
    OUTPUT_HEADER_MINIMUM_MODIFY("出参header最小值修改"),
    OUTPUT_HEADER_EXCLUSIVE_MINIMUM_MODIFY("出参header不含最小值修改"),
    OUTPUT_HEADER_MAXLENGTH_MODIFY("出参header最大长度修改"),
    OUTPUT_HEADER_MINLENGTH_MODIFY("出参header最小长度修改"),
    OUTPUT_HEADER_PATTERN_MODIFY("出参header模式修改"),
    OUTPUT_HEADER_MAX_ITEMS_MODIFY("出参header数组最大长度修改"),
    OUTPUT_HEADER_MIN_ITEMS_MODIFY("出参header数组最小长度修改"),
    OUTPUT_HEADER_UNIQUE_ITEMS_MODIFY("出参header数组唯一性修改"),
    OUTPUT_HEADER_ENUM_VALUES_MODIFY("出参header可选值列表修改"),
    OUTPUT_HEADER_MULTIPLE_OF_MODIFY("出参header multipleOf修改"),

    OUTPUT_HEADER_ITEM_TYPE_MODIFY("出参header item字段类型修改"),
    OUTPUT_HEADER_ITEM_FORMAT_MODIFY("出参header item字段格式修改"),
    OUTPUT_HEADER_ITEM_COLLECTION_FORMAT_MODIFY("出参header item集合格式修改"),
    OUTPUT_HEADER_ITEM_DEFAULT_VALUE_MODIFY("出参header item默认值修改"),
    OUTPUT_HEADER_ITEM_MAXIMUM_MODIFY("出参header item最大值修改"),
    OUTPUT_HEADER_ITEM_EXCLUSIVE_MAXIMUM_MODIFY("出参header item不含最大值修改"),
    OUTPUT_HEADER_ITEM_MINIMUM_MODIFY("出参header item最小值修改"),
    OUTPUT_HEADER_ITEM_EXCLUSIVE_MINIMUM_MODIFY("出参header item不含最小值修改"),
    OUTPUT_HEADER_ITEM_MAXLENGTH_MODIFY("出参header item最大长度修改"),
    OUTPUT_HEADER_ITEM_MINLENGTH_MODIFY("出参header item最小长度修改"),
    OUTPUT_HEADER_ITEM_PATTERN_MODIFY("出参header item模式修改"),
    OUTPUT_HEADER_ITEM_MAX_ITEMS_MODIFY("出参header item数组最大长度修改"),
    OUTPUT_HEADER_ITEM_MIN_ITEMS_MODIFY("出参header item数组最小长度修改"),
    OUTPUT_HEADER_ITEM_UNIQUE_ITEMS_MODIFY("出参header item数组唯一性修改"),
    OUTPUT_HEADER_ITEM_ENUM_VALUES_MODIFY("出参header item可选值列表修改"),
    OUTPUT_HEADER_ITEM_MULTIPLE_OF_MODIFY("出参header item multipleOf修改"),

    OUTPUT_EXAMPLE_MODIFY("出参例子修改")
    ;

    private String name;

    ChangeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
