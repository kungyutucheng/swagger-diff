package com.kungyu.model.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kungyu.enums.MethodType;
import com.kungyu.util.HttpUtil;
import gherkin.lexer.Pa;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author wengyongcheng
 * @since 2020/7/2 3:29 下午
 */
public class PathTest {

    @Test
    public void test() {
        String result = HttpUtil.doPost("http://localhost:9111/v2/api-docs", null);
        result = StringUtils.replace(result, "$ref", "ref");
        JSONObject json = JSON.parseObject(result);
        JSONObject pathsJson = json.getJSONObject("paths");
        for (String url : pathsJson.keySet()) {
            JSONObject pathJson = new JSONObject();
            pathJson.put(url, pathsJson.getJSONObject(url));
            Path path = Path.convertToPath(pathJson);
            System.out.println(JSON.toJSONString(path));
            System.out.println("-------------");
        }
    }


}