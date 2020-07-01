package com.kungyu.model.v2;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author wengyongcheng
 * @since 2020/6/30 10:07 上午
 */
public class Method {

    private List<String> consumes;

    private String description;

    private List<Parameter> parameters;

    private List<String> produces;

    private Map<Integer, Response> responses;

    private String summary;

    private List<String> tags;

    public static Method convertToMethod(JSONObject jsonObject) {
        return new MethodConverter().doBackward(jsonObject);
    }

    public static class MethodConverter extends Converter<Method, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Method method) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Method doBackward(@NotNull JSONObject methodJson) {
            Method method = new Method();

            // 处理consumes
            // eg：consumes: ["application/json"]
            JSONArray consumesJsonArray = methodJson.getJSONArray("consumes");
            if (consumesJsonArray != null && consumesJsonArray.size() > 0) {
                method.setConsumes(consumesJsonArray.toJavaList(String.class));
            }

            // 处理description
            method.setDescription(methodJson.getString("description"));

            // 处理parameter
            // eg：parameters: [{in: "body", name: "req", description: "req", required: true,…}]
            JSONArray parameterJsonArray = methodJson.getJSONArray("parameters");
            if (parameterJsonArray != null && parameterJsonArray.size() > 0) {
                List<Parameter> parameterList = new ArrayList<>();
                for (int i = 0;i < parameterJsonArray.size(); i++) {
                    parameterList.add(Parameter.convertToParameter(parameterJsonArray.getJSONObject(i)));
                }
                method.setParameters(parameterList);
            }

            // 处理produces
            // eg：produces: ["application/json"]
            JSONArray producesJsonArray = methodJson.getJSONArray("produces");
            if (producesJsonArray != null && producesJsonArray.size() > 0) {
                method.setProduces(producesJsonArray.toJavaList(String.class));
            }

            // 处理responses
            // eg：responses: {200: {description: "OK", schema: {$ref: "#/definitions/JsonResult«AccountGetNeedGuideStatusResp»"}},…}
            JSONObject responseJson = methodJson.getJSONObject("responses");
            if (responseJson != null) {
                Map<Integer, Response> responseMap = new HashMap<>();
                for (String key : responseJson.keySet()) {
                    Integer httpStatusCode = Integer.valueOf(key);
                    responseMap.computeIfAbsent(httpStatusCode, (tmp) -> Response.convertToResponse(responseJson.getJSONObject(key)));
                }
                method.setResponses(responseMap);
            }

            // 处理summary
            // eg：summary: "查询客户是否需要引导状态"
            method.setSummary(methodJson.getString("summary"));

            // 处理tags
            // eg：tags: ["App三湘用户相关接口"]
            JSONArray tagsJsonArray = methodJson.getJSONArray("tags");
            if (tagsJsonArray != null && tagsJsonArray.size() > 0) {
                method.setTags(tagsJsonArray.toJavaList(String.class));
            }

            return method;
        }
    }


    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public Map<Integer, Response> getResponses() {
        return responses;
    }

    public void setResponses(Map<Integer, Response> responses) {
        this.responses = responses;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }




}
