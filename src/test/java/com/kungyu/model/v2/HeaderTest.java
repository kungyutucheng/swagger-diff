package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.tools.internal.xjc.reader.dtd.bindinfo.BIAttribute;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author wengyongcheng
 * @since 2020/7/1 6:06 下午
 */
public class HeaderTest {

    @Test
    public void test() {
        JSONObject headerJson = new JSONObject();

        headerJson.put("description", "ds");
        headerJson.put("type", "string");
        headerJson.put("format", "string");
        JSONObject itemJson = new JSONObject();
        itemJson.put("type", "string");
        headerJson.put("items", itemJson);
        headerJson.put("collectionFormat", "collectionFormat");
        headerJson.put("defaultValue", "defaultValue");
        headerJson.put("maximum", BigDecimal.ZERO);
        headerJson.put("exclusiveMaximum", true);
        headerJson.put("minimum", BigDecimal.ZERO);
        headerJson.put("exclusiveMinimum", false);
        headerJson.put("maxLength", 11);
        headerJson.put("minLength", 12);
        headerJson.put("pattern", "pattern");
        headerJson.put("maxItems", 1);
        headerJson.put("minItems", 2);
        headerJson.put("uniqueItems", true);
        JSONArray enumArray = new JSONArray();
        enumArray.add("das");
        headerJson.put("enum", enumArray);
        headerJson.put("multipleOf", BigDecimal.ZERO);

        Header header = Header.convertToHeader(headerJson);
        assertEquals(headerJson.getString("description"),header.getDescription());
        assertEquals(headerJson.getString("type"),header.getType() );
        assertEquals(headerJson.getString("format"),header.getFormat());
        assertEquals(Item.convertToItem(itemJson),header.getItems());
        assertEquals(headerJson.getString("collectionFormat"),header.getCollectionFormat());
        assertEquals(headerJson.getString("defaultValue"), header.getDefaultValue());
        assertEquals(headerJson.getBigDecimal("maximum"),header.getMaximum() );
        assertEquals(headerJson.getBoolean("exclusiveMaximum"),header.getExclusiveMaximum());
        assertEquals(headerJson.getBigDecimal("minimum"),header.getMinimum());
        assertEquals(headerJson.getBoolean("exclusiveMinimum"),header.getExclusiveMinimum());
        assertEquals(headerJson.getInteger("maxLength"), header.getMaxLength());
        assertEquals(headerJson.getInteger("minLength"),header.getMinLength());
        assertEquals(headerJson.getString("pattern"),header.getPattern());
        assertEquals(headerJson.getInteger("maxItems"),header.getMaxItems());
        assertEquals(headerJson.getInteger("minItems"),header.getMinItems());
        assertEquals(headerJson.getBoolean("uniqueItems"),header.getUniqueItems());
        assertEquals(enumArray.toJavaList(String.class), header.getEnumValues());
        assertEquals(headerJson.getBigDecimal("multipleOf"),header.getMultipleOf());

    }

}