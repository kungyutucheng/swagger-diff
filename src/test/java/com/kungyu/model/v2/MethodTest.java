package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author wengyongcheng
 * @since 2020/7/1 3:21 下午
 */
public class MethodTest {

    @Test
    public void test() {
        JSONObject methodJson = new JSONObject();

        JSONArray consumesArray = new JSONArray();
        consumesArray.add("application/json");
        methodJson.put("consumes", consumesArray);

        methodJson.put("description", "description");

        JSONArray producesArray = new JSONArray();
        producesArray.add("application/json;charset=utf-8");
        methodJson.put("produces", producesArray);

        methodJson.put("summary", "summary");

        JSONArray tagsArray = new JSONArray();
        tagsArray.add("App三湘用户相关接口");
        methodJson.put("tags", tagsArray);

        Method method = Method.convertToMethod(methodJson);
        System.out.println(method.getConsumes());
        System.out.println(method.getDescription());
        System.out.println(method.getProduces());
        System.out.println(method.getSummary());
        System.out.println(method.getTags());
    }

}