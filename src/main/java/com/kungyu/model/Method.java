package com.kungyu.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

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
