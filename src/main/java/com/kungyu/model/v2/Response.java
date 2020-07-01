package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wengyongcheng
 * @since 2020/6/30 11:00 上午
 */
public class Response {

    private String description;

    private Schema schema;

    private Map<String, Header> headers;

    private Map<String, String> examples;

    public static Response convertToResponse(JSONObject responseJson) {
        return new ResponseConverter().doBackward(responseJson);
    }

    private static final class ResponseConverter extends Converter<Response, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Response response) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Response doBackward(@NotNull JSONObject responseJson) {
            Response response = new Response();

            // 处理description
            // eg："description": "A simple string response"
            response.setDescription(responseJson.getString("description"));

            // 处理schema
            // eg："schema": {
            //    "type": "string"
            //  }
            JSONObject schemaJson = responseJson.getJSONObject("schema");
            if (schemaJson != null) {
                response.setSchema(Schema.convertToSchema(schemaJson));
            }

            // 处理headers
            JSONObject headerJson = responseJson.getJSONObject("headers");
            if (headerJson != null) {
                Map<String, Header> headerMap = new HashMap<>();
                for (String key : headerJson.keySet()) {
                    headerMap.computeIfAbsent(key, (tmp) -> Header.convertToHeader(headerJson.getJSONObject(key)));
                }
                response.setHeaders(headerMap);
            }

            JSONObject exampleJson = responseJson.getJSONObject("examples");
            if (exampleJson != null) {
                Map<String, String> exampleMap = new HashMap<>();
                for (String key : exampleJson.keySet()) {
                    exampleMap.put(key, exampleJson.getString(key));
                }
                response.setExamples(exampleMap);
            }
            return response;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Map<String, Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    public Map<String, String> getExamples() {
        return examples;
    }

    public void setExamples(Map<String, String> examples) {
        this.examples = examples;
    }
}
