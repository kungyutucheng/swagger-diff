package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wengyongcheng
 * @since 2020/6/30 2:55 下午
 */
public class Item {

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

    public static Item convertToItem(JSONObject itemJson) {
        return new ItemConverter().doBackward(itemJson);
    }

    private static final class ItemConverter extends Converter<Item, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Item item) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Item doBackward(@NotNull JSONObject itemJson) {
            Item item = new Item();

            item.setType(itemJson.getString("type"));
            item.setFormat(itemJson.getString("format"));

            JSONObject innerItemJson = itemJson.getJSONObject("items");
            if (innerItemJson != null) {
                item.setItems(Item.convertToItem(innerItemJson));
            }

            item.setCollectionFormat(itemJson.getString("collectionFormat"));
            item.setDefaultValue(itemJson.getString("defaultValue"));

            item.setMaximum(itemJson.getBigDecimal("maximum"));
            item.setExclusiveMaximum(itemJson.getBoolean("exclusiveMaximum"));
            item.setMinimum(itemJson.getBigDecimal("minimum"));
            item.setExclusiveMinimum(itemJson.getBoolean("exclusiveMinimum"));
            item.setMaxLength(itemJson.getInteger("maxLength"));
            item.setMinLength(itemJson.getInteger("minLength"));
            item.setPattern(itemJson.getString("pattern"));
            item.setMaxItems(itemJson.getInteger("maxItems"));
            item.setMinItems(itemJson.getInteger("minItems"));
            item.setUniqueItems(itemJson.getBoolean("uniqueItems"));
            JSONArray enumValueArray = itemJson.getJSONArray("enum");
            if (enumValueArray != null && enumValueArray.size() > 0) {
                item.setEnumValues(enumValueArray.toJavaList(String.class));
            }

            item.setMultipleOf(itemJson.getBigDecimal("multipleOf"));

            return item;
        }
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

    public com.kungyu.model.v2.Item getItems() {
        return items;
    }

    public void setItems(com.kungyu.model.v2.Item items) {
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
