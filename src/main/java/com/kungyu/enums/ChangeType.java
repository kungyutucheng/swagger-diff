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
    OUTPUT_PARAMETER_TYPE_MODIFY("出参类型修改"),
    OUTPUT_PARAMETER_DESC_MODIFY("出参描述修改"),
    OUTPUT_OBJECT_DESC_MODIFY("出参对象描述修改"),
    OUTPUT_HEADER_ADD("新增出参header"),
    OUTPUT_HEADER_DELETE("删除出参header"),
    OUTPUT_HEADER_DESC_MODIFY("出参header描述修改"),
    OUTPUT_HEADER_TYPE_MODIFY("出参header字段类型修改"),
    OUTPUT_HEADER_FORMAT_MODIFY("出参header字段格式修改"),
    OUTPUT_HEADER_COLLECTION_FORMAT_MODIFY("出参header集合格式修改"),
    OUTPUT_HEADER_DEFAULT_VALUE_MODIFY("出参header默认值修改"),
    OUTPUT_HEADER_MAXIMUM_MODIFY("出参header最大值修改"),
    OUTPUT_HEADER_EXCLUSIVE_MAXIMUM_MODIFY("出参header唯一最大值修改"),
    OUTPUT_HEADER_MINIMUM_MODIFY("出参header最小值修改"),
    OUTPUT_HEADER_EXCLUSIVE_MINIMUM_MODIFY("出参header唯一最小值修改"),
    OUTPUT_HEADER_MAXLENGTH_MODIFY("出参header最大长度修改"),
    OUTPUT_HEADER_MINLENGTH_MODIFY("出参header最小长度修改"),
    OUTPUT_HEADER_MODIFY("出参header模式修改"),
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
