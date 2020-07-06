package com.kungyu.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kungyu.enums.ChangeType;
import com.kungyu.enums.MethodType;
import com.kungyu.enums.SchemaType;
import com.kungyu.model.diff.DiffResult;
import com.kungyu.model.v2.*;
import com.kungyu.util.HttpUtil;
import gherkin.lexer.Pa;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * @author wengyongcheng
 * @since 2020/7/2 6:26 下午
 */
public class DiffTask implements Callable<List<DiffResult>> {

    private String newUrl;

    private String origUrl;

    private List<Definition> newDefinitionList = new ArrayList<>();
    private List<Definition> origDefinitionList = new ArrayList<>();

    private List<DiffResult> diffResultList = new ArrayList<>();

    private String currentDiffPath = null;

    public DiffTask(@NotNull String newUrl, @NotNull String origUrl) {
        this.newUrl = newUrl;
        this.origUrl = origUrl;
    }

    @Override
    public List<DiffResult> call() throws Exception {
        String newUrlResponseStr = HttpUtil.doPost(newUrl, null);
        String origUrlResponseStr = HttpUtil.doPost(origUrl, null);

        // fastjson解析带有$符号的key会出现问题，所以需要全局替换$ref为ref
        String searchString = "$ref";
        String replaceString = "ref";
        newUrlResponseStr = StringUtils.replace(newUrlResponseStr, searchString, replaceString);
        origUrlResponseStr = StringUtils.replace(origUrlResponseStr, searchString, replaceString);

        JSONObject newJson = JSON.parseObject(newUrlResponseStr);
        JSONObject origJson = JSON.parseObject(origUrlResponseStr);

        JSONObject newDefinitionsJson = newJson.getJSONObject("definitions");
        for (String name : newDefinitionsJson.keySet()) {
            JSONObject definitionJson = new JSONObject();
            definitionJson.put(name, newDefinitionsJson.getJSONObject(name));
            Definition definition = Definition.convertToDefinition(definitionJson);
            newDefinitionList.add(definition);
        }

        JSONObject origDefinitionsJson = origJson.getJSONObject("definitions");
        for (String name : origDefinitionsJson.keySet()) {
            JSONObject definitionJson = new JSONObject();
            definitionJson.put(name, origDefinitionsJson.getJSONObject(name));
            Definition definition = Definition.convertToDefinition(definitionJson);
            origDefinitionList.add(definition);
        }


        JSONObject newPathsJson = newJson.getJSONObject("paths");
        JSONObject origPathsJson = origJson.getJSONObject("paths");

        diffPath(newPathsJson, origPathsJson);

        return diffResultList;
    }

    private void diffPath(JSONObject newPathsJson, JSONObject origPathsJson) {
        Set<String> newPathKeySet = newPathsJson.keySet();
        Set<String> origPathKeySet = origPathsJson.keySet();
        for (String newPath : newPathKeySet) {
            // 如果旧文档也存在对应路径，则进入path的具体对比
            if (origPathKeySet.contains(newPath)) {
                this.doDiffPath(Path.convertToPath(newPathsJson.getJSONObject(newPath)), Path.convertToPath(origPathsJson.getJSONObject(newPath)));
            } else {
                // 否则，说明是新增的path
                this.buildDiffResult(newPath,null,null,null,ChangeType.API_ADD,null);
            }
        }

        // 补充删除api的部分
        for (String origPath : origPathKeySet) {
            if (!newPathKeySet.contains(origPath)) {
                this.buildDiffResult(origPath,null,null,null,ChangeType.API_DELETE,null);
            }
        }

    }

    private void doDiffPath(Path newPath, Path origPath) {
        for (MethodType newPathMethod : newPath.getMethodList().keySet()) {
            if (origPath.getMethodList().containsKey(newPathMethod)) {
                // 新旧文档存在同样的method，对比method
                // 保存当前对比的path，方便构造结果的时候取出使用
                currentDiffPath = newPathMethod.getName();
                this.diffMethod(newPath.getMethodList().get(newPathMethod), origPath.getMethodList().get(newPathMethod));
            } else {
                // 新增的method
                this.buildDiffResult(newPath.getPath(), null, newPathMethod.getName(), null, ChangeType.METHOD_ADD, null);
            }

        }

        // 删除的method
        for (MethodType prigPathMethod : origPath.getMethodList().keySet()) {
            if (!newPath.getMethodList().containsKey(prigPathMethod)) {
                this.buildDiffResult(origPath.getPath(),prigPathMethod.getName(),null,null,ChangeType.METHOD_DELETE,null);
            }
        }

    }

    private void diffMethod(Method newMethod, Method origMethod) {
        this.diffConsumes(newMethod.getConsumes(),origMethod.getConsumes(),ChangeType.API_CONSUMES_MODIFY);

        this.diffDesc(newMethod.getDescription(), origMethod.getDescription(), ChangeType.API_DESC_MODIFY);

        this.diffParameter(newMethod.getParameters(),origMethod.getParameters());

        this.diffProduces(newMethod.getProduces(), origMethod.getProduces(), ChangeType.API_PRODUCES_MODIFY);

        this.diffResponse(newMethod.getResponses(), origMethod.getResponses());

        this.diffSummary(newMethod.getSummary(), origMethod.getSummary());

        this.diffTags(newMethod.getTags(), origMethod.getTags());
    }

    private void diffTags(List<String> newTags, List<String> origTags) {
        diffStringList(newTags,origTags,ChangeType.API_TAGS_MODIFY);
    }

    private void diffSummary(String newSummary, String origSummary) {
        diffString(newSummary, origSummary, ChangeType.API_SUMMARY_MODIFY);
    }

    private void diffResponse(Map<Integer, Response> newResponses, Map<Integer, Response> origResponses) {
        for (Integer newHttpCode : newResponses.keySet()) {
            if (origResponses.containsKey(newHttpCode)) {
                // 新旧版本共同持有的响应状态吗，进行响应参数具体对比
                this.doDiffResponse(newResponses.get(newHttpCode), origResponses.get(newHttpCode));
            } else {
                // 新版本新增响应状态码
                this.buildDiffResult(currentDiffPath,null,newHttpCode + "",null,ChangeType.HTTP_CODE_ADD,null);
            }
        }

        // 新版本删除响应状态码
        for (Integer origHttpCode : origResponses.keySet()) {
            if (newResponses.containsKey(origHttpCode)) {
                this.buildDiffResult(currentDiffPath, origHttpCode + "", null, null, ChangeType.HTTP_CODE_DELETE, null);
            }
        }
    }

    private void doDiffResponse(Response newResponse, Response origResponse) {
        diffString(newResponse.getDescription(),origResponse.getDescription(),null,ChangeType.OUTPUT_OBJECT_DESC_MODIFY,null);
        diffSchema(newResponse.getSchema(), origResponse.getSchema(), SchemaType.RESPONSE);

        diffHeaders(newResponse.getHeaders(), origResponse.getHeaders());

        diffExamples(newResponse.getExamples(), origResponse.getExamples());

    }

    private void diffExamples(Map<String, String> newExampleMap, Map<String, String> origExampleMap) {
        diffString(newExampleMap.toString(), origExampleMap.toString(), ChangeType.OUTPUT_EXAMPLE_MODIFY);
    }

    private void diffHeaders(Map<String, Header> newHeaderMap, Map<String, Header> origHeaderMap) {
        for (String newHeaderKey : newHeaderMap.keySet()) {
            if (origHeaderMap.containsKey(newHeaderKey)) {
                this.diffHeader(newHeaderMap.get(newHeaderKey), origHeaderMap.get(newHeaderKey));
            } else {
                this.buildDiffResult(currentDiffPath,null,newHeaderKey,null,ChangeType.OUTPUT_HEADER_ADD,null);
            }
        }

        for (String origHeaderKey : origHeaderMap.keySet()) {
            if (newHeaderMap.containsKey(origHeaderKey)) {
                this.buildDiffResult(currentDiffPath, origHeaderKey, null, null, ChangeType.OUTPUT_HEADER_DELETE, null);
            }
        }
    }

    private void diffHeader(Header newHeader, Header origHeader) {
        diffDesc(newHeader.getDescription(),origHeader.getDescription(),ChangeType.OUTPUT_HEADER_DESC_MODIFY);
    }

    private void diffSchema(Schema newSchema, Schema origSchema, SchemaType schemaType) {
    }

    private void diffParameter(List<Parameter> newParameters, List<Parameter> origParameters) {
        if (CollectionUtils.isNotEmpty(newParameters) && CollectionUtils.isNotEmpty(origParameters)) {
            // 将list类型的parameter转化为<name,parameter>类型的map，方便对比数据
            Map<String, Parameter> newParameterMap = newParameters.stream().collect(Collectors.toMap(Parameter::getName, parameter -> parameter));
            Map<String, Parameter> origParameterMap = origParameters.stream().collect(Collectors.toMap(Parameter::getName, parameter -> parameter));

            for (String newParameterName : newParameterMap.keySet()) {
                if (origParameterMap.containsKey(newParameterName)) {
                    // 新旧版本共同持有的参数，进行具体对比
                    this.doDiffParameter(newParameterMap.get(newParameterName), origParameterMap.get(newParameterName));
                } else {
                    // 新版本新增参数
                    this.buildDiffResult(currentDiffPath, null, newParameterName, newParameterName, ChangeType.INPUT_PARAMETER_ADD, null);
                }
            }

            // 新版本删除参数
            for (String origParameterKey : origParameterMap.keySet()) {
                if (!newParameterMap.containsKey(origParameterKey)) {
                    this.buildDiffResult(currentDiffPath, origParameterKey, null, origParameterKey, ChangeType.INPUT_PARAMETER_DELETE, null);
                }
            }
        } else if (CollectionUtils.isNotEmpty(newParameters)) {
            // 旧版本不存在参数，新版本新增参数
            for (Parameter newParameter : newParameters) {
                this.buildDiffResult(currentDiffPath, null, newParameter.getName(), newParameter.getName(), ChangeType.INPUT_PARAMETER_ADD, null);
            }
        } else if(CollectionUtils.isNotEmpty(origParameters)) {
            // 旧版本存在参数，新版本完全删除
            for (Parameter origParameter : origParameters) {
                this.buildDiffResult(currentDiffPath, origParameter.getName(), null, origParameter.getName(), ChangeType.INPUT_PARAMETER_DELETE, null);
            }
        }
    }

    private void doDiffParameter(Parameter newParameter, Parameter origParameter) {

    }

    private void diffConsumes(List<String> newConsumes, List<String> origCosumes, ChangeType changeType) {
        diffStringList(newConsumes,origCosumes,changeType);
    }

    private void diffDesc(String newDesc, String origDesc, ChangeType changeType) {
        diffString(newDesc,origDesc,changeType);
    }

    private void diffProduces(List<String> newProduces, List<String> origProduces, ChangeType changeType) {
        diffStringList(newProduces,origProduces,changeType);
    }

    private void diffStringList(List<String> newList, List<String> origList, ChangeType changeType) {
        diffString(newList.toString(),origList.toString(),changeType);
    }

    private void diffString(String newString, String origString, ChangeType changeType) {
        this.diffString(newString, origString, null, changeType, null);
    }

    private void diffString(String newString, String origString, String fieldName,ChangeType changeType, String desc) {
        if (StringUtils.equals(newString,origString)) {
            this.buildDiffResult(currentDiffPath,origString,newString,null,changeType,null);
        }
    }

    private void buildDiffResult(String url, String origValue, String newValue, String fieldName, ChangeType changeType, String desc) {
        DiffResult diffResult = new DiffResult();
        diffResult.setUrl(url);
        diffResult.setOrigValue(origValue);
        diffResult.setNewValue(newValue);
        diffResult.setFieldName(fieldName);
        diffResult.setChangeType(changeType);
        diffResult.setDesc(desc);
        diffResultList.add(diffResult);
    }


}
