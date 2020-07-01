package com.kungyu.model.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author wengyongcheng
 * @since 2020/7/1 5:40 下午
 */
public class ResponseTest {

    @Test
    public void test() {
        JSONObject responseJson = new JSONObject();
        responseJson.put("description", "fds");

        JSONObject headersJson = new JSONObject();

        JSONObject headerJson = new JSONObject();
        headerJson.put("description", "headerDesc");
        headersJson.put("222", headerJson);
        responseJson.put("headers", headersJson);

        Response response = Response.convertToResponse(responseJson);

        Assert.assertEquals(responseJson.getString("description"), response.getDescription());
    }

}