package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import com.kungyu.enums.Format;
import com.kungyu.enums.InType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wengyongcheng
 * @since 2020/6/30 10:59 上午
 */
public class Parameter {

    private String name;

    private InType in;

    private String description;

    private Boolean required;


    // in = body
    private Schema schema;

    // in = others
    private String type;

    private String format;

    private Boolean allowEmptyValue;

    private Item items;

    private String collectionFormat;

    private String defaultValue;

    private BigDecimal maximum;

    private Boolean exclusiveMaximum;

    private BigDecimal minimum;

    private Boolean exclusiveMinimum;

    private Integer maxLength;

    private Integer minLength;

    private String pattern;

    private Integer maxItems;

    private Integer minItems;

    private Boolean uniqueItems;

    private List<String> enumValues;

    private BigDecimal multipleOf;

    public static Parameter convertToParameter(JSONObject parameterJson) {
        return new ParameterConverter().doBackward(parameterJson);
    }

    private static final class ParameterConverter extends Converter<Parameter, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Parameter parameter) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Parameter doBackward(@NotNull JSONObject parameterJson) {
            Parameter parameter = new Parameter();

            parameter.setName(parameterJson.getString("name"));
            InType.getByName(parameterJson.getString("in")).ifPresent(parameter::setIn);
            parameter.setDescription(parameterJson.getString("description"));
            parameter.setRequired(parameterJson.getBoolean("required"));

            JSONObject schemaJson = parameterJson.getJSONObject("schema");
            if (schemaJson != null) {
                parameter.setSchema(Schema.convertToSchema(schemaJson));
            }
            parameter.setType(parameterJson.getString("type"));
            parameter.setFormat(parameterJson.getString("format"));
            parameter.setAllowEmptyValue(parameterJson.getBoolean("allowEmptyValue"));

            JSONObject itemJson = parameterJson.getJSONObject("items");
            if (itemJson != null) {
                parameter.setItems(Item.convertToItem(itemJson));
            }

            parameter.setCollectionFormat(parameterJson.getString("collectionFormat"));
            parameter.setDefaultValue(parameterJson.getString("defaultValue"));

            parameter.setMaximum(parameterJson.getBigDecimal("maximum"));
            parameter.setExclusiveMaximum(parameterJson.getBoolean("exclusiveMaximum"));
            parameter.setMinimum(parameterJson.getBigDecimal("minimum"));
            parameter.setExclusiveMinimum(parameterJson.getBoolean("exclusiveMinimum"));
            parameter.setMaxLength(parameterJson.getInteger("maxLength"));
            parameter.setMinLength(parameterJson.getInteger("minLength"));
            parameter.setPattern(parameterJson.getString("pattern"));
            parameter.setMaxItems(parameterJson.getInteger("maxItems"));
            parameter.setMinItems(parameterJson.getInteger("minItems"));
            parameter.setUniqueItems(parameterJson.getBoolean("uniqueItems"));
            JSONArray enumValueArray = parameterJson.getJSONArray("enum");
            if (enumValueArray != null && enumValueArray.size() > 0) {
                parameter.setEnumValues(enumValueArray.toJavaList(String.class));
            }

            parameter.setMultipleOf(parameterJson.getBigDecimal("multipleOf"));

            return parameter;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InType getIn() {
        return in;
    }

    public void setIn(InType in) {
        this.in = in;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Boolean getAllowEmptyValue() {
        return allowEmptyValue;
    }

    public void setAllowEmptyValue(Boolean allowEmptyValue) {
        this.allowEmptyValue = allowEmptyValue;
    }

    public Item getItems() {
        return items;
    }

    public void setItems(Item items) {
        this.items = items;
    }

    public String getCollectionFormat() {
        return collectionFormat;
    }

    public void setCollectionFormat(String collectionFormat) {
        this.collectionFormat = collectionFormat;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }

    public BigDecimal getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
    }
}
