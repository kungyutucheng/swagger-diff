package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wengyongcheng
 * @since 2020/6/30 3:06 下午
 */
public class Header {

    private String description;

    private String type;

    private String format;

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

    public static Header convertToHeader(JSONObject headerJson) {
        return new HeaderConverter().doBackward(headerJson);
    }

    private static final class HeaderConverter extends Converter<Header, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Header header) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Header doBackward(@NotNull JSONObject headerJson) {
            Header header = new Header();

            header.setDescription(headerJson.getString("description"));
            header.setType(headerJson.getString("type"));
            header.setFormat(headerJson.getString("format"));

            JSONObject itemJson = headerJson.getJSONObject("items");
            if (itemJson != null) {
                header.setItems(Item.convertToItem(itemJson));
            }

            header.setCollectionFormat(headerJson.getString("collectionFormat"));
            header.setDefaultValue(headerJson.getString("defaultValue"));

            header.setMaximum(headerJson.getBigDecimal("maximum"));
            header.setExclusiveMaximum(headerJson.getBoolean("exclusiveMaximum"));
            header.setMinimum(headerJson.getBigDecimal("minimum"));
            header.setExclusiveMinimum(headerJson.getBoolean("exclusiveMinimum"));
            header.setMaxLength(headerJson.getInteger("maxLength"));
            header.setMinLength(headerJson.getInteger("minLength"));
            header.setPattern(headerJson.getString("pattern"));
            header.setMaxItems(headerJson.getInteger("maxItems"));
            header.setMinItems(headerJson.getInteger("minItems"));
            header.setUniqueItems(headerJson.getBoolean("uniqueItems"));
            JSONArray enumValueArray = headerJson.getJSONArray("enum");
            if (enumValueArray != null && enumValueArray.size() > 0) {
                header.setEnumValues(enumValueArray.toJavaList(String.class));
            }

            header.setMultipleOf(headerJson.getBigDecimal("multipleOf"));

            return header;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
