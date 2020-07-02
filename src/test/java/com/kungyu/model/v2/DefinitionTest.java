package com.kungyu.model.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kungyu.util.HttpUtil;
import gherkin.lexer.De;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author wengyongcheng
 * @since 2020/7/2 5:24 下午
 */
public class DefinitionTest {

    @Test
    public void test() {
        String result = HttpUtil.doPost("http://localhost:9111/v2/api-docs", null);
        result = StringUtils.replace(result, "$ref", "ref");
        JSONObject json = JSON.parseObject(result);
        JSONObject pathsJson = json.getJSONObject("definitions");
        for (String name : pathsJson.keySet()) {
            JSONObject definitionJson = new JSONObject();
            definitionJson.put(name, pathsJson.getJSONObject(name));
            Definition definition = Definition.convertToDefinition(definitionJson);

            System.out.println(JSON.toJSONString(definition));
        }
    }

}