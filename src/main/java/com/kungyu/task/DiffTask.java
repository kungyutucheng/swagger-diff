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

import java.math.BigDecimal;
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

    private String currentFieldName = null;

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
        diffString(newHeader.getType(), origHeader.getType(), ChangeType.OUTPUT_HEADER_TYPE_MODIFY);
        diffString(newHeader.getFormat(), origHeader.getFormat(), ChangeType.OUTPUT_HEADER_FORMAT_MODIFY);

        diffItems(newHeader.getItems(), origHeader.getItems());

        diffString(newHeader.getCollectionFormat(), origHeader.getCollectionFormat(), ChangeType.OUTPUT_HEADER_COLLECTION_FORMAT_MODIFY);
        diffString(newHeader.getDefaultValue(), origHeader.getDefaultValue(), ChangeType.OUTPUT_HEADER_DEFAULT_VALUE_MODIFY);
        diffBigDecimal(newHeader.getMaximum(), origHeader.getMaximum(), ChangeType.OUTPUT_HEADER_MAXIMUM_MODIFY);
        diffBoolean(newHeader.getExclusiveMaximum(),origHeader.getExclusiveMaximum(),ChangeType.OUTPUT_HEADER_EXCLUSIVE_MAXIMUM_MODIFY);
        diffBigDecimal(newHeader.getMinimum(), origHeader.getMinimum(), ChangeType.OUTPUT_HEADER_MINIMUM_MODIFY);
        diffBoolean(newHeader.getExclusiveMinimum(), origHeader.getExclusiveMinimum(), ChangeType.OUTPUT_HEADER_EXCLUSIVE_MINIMUM_MODIFY);
        diffInteger(newHeader.getMaxLength(), origHeader.getMaxLength(), ChangeType.OUTPUT_HEADER_MAXLENGTH_MODIFY);
        diffInteger(newHeader.getMinLength(), origHeader.getMinLength(), ChangeType.OUTPUT_HEADER_MINLENGTH_MODIFY);
        diffString(newHeader.getPattern(), origHeader.getPattern(), ChangeType.OUTPUT_HEADER_PATTERN_MODIFY);
        diffInteger(newHeader.getMaxItems(), origHeader.getMaxItems(), ChangeType.OUTPUT_HEADER_MAX_ITEMS_MODIFY);
        diffInteger(newHeader.getMinItems(), origHeader.getMinItems(), ChangeType.OUTPUT_HEADER_MIN_ITEMS_MODIFY);
        diffBoolean(newHeader.getUniqueItems(), origHeader.getUniqueItems(), ChangeType.OUTPUT_HEADER_UNIQUE_ITEMS_MODIFY);
        diffStringList(newHeader.getEnumValues(), origHeader.getEnumValues(), ChangeType.OUTPUT_HEADER_ENUM_VALUES_MODIFY);
        diffBigDecimal(newHeader.getMultipleOf(),origHeader.getMultipleOf(),ChangeType.OUTPUT_HEADER_MULTIPLE_OF_MODIFY);
    }

    private void diffItems(Item newItem, Item origItem) {

        diffString(newItem.getType(), origItem.getType(), ChangeType.OUTPUT_HEADER_ITEM_TYPE_MODIFY);
        diffString(newItem.getFormat(), origItem.getFormat(), ChangeType.OUTPUT_HEADER_ITEM_FORMAT_MODIFY);

        diffString(newItem.getCollectionFormat(), origItem.getCollectionFormat(), ChangeType.OUTPUT_HEADER_ITEM_COLLECTION_FORMAT_MODIFY);
        diffString(newItem.getDefaultValue(), origItem.getDefaultValue(), ChangeType.OUTPUT_HEADER_ITEM_DEFAULT_VALUE_MODIFY);
        diffBigDecimal(newItem.getMaximum(), origItem.getMaximum(), ChangeType.OUTPUT_HEADER_ITEM_MAXIMUM_MODIFY);
        diffBoolean(newItem.getExclusiveMaximum(),origItem.getExclusiveMaximum(),ChangeType.OUTPUT_HEADER_ITEM_EXCLUSIVE_MAXIMUM_MODIFY);
        diffBigDecimal(newItem.getMinimum(), origItem.getMinimum(), ChangeType.OUTPUT_HEADER_ITEM_MINIMUM_MODIFY);
        diffBoolean(newItem.getExclusiveMinimum(), origItem.getExclusiveMinimum(), ChangeType.OUTPUT_HEADER_ITEM_EXCLUSIVE_MINIMUM_MODIFY);
        diffInteger(newItem.getMaxLength(), origItem.getMaxLength(), ChangeType.OUTPUT_HEADER_ITEM_MAXLENGTH_MODIFY);
        diffInteger(newItem.getMinLength(), origItem.getMinLength(), ChangeType.OUTPUT_HEADER_ITEM_MINLENGTH_MODIFY);
        diffString(newItem.getPattern(), origItem.getPattern(), ChangeType.OUTPUT_HEADER_ITEM_PATTERN_MODIFY);
        diffInteger(newItem.getMaxItems(), origItem.getMaxItems(), ChangeType.OUTPUT_HEADER_ITEM_MAX_ITEMS_MODIFY);
        diffInteger(newItem.getMinItems(), origItem.getMinItems(), ChangeType.OUTPUT_HEADER_ITEM_MIN_ITEMS_MODIFY);
        diffBoolean(newItem.getUniqueItems(), origItem.getUniqueItems(), ChangeType.OUTPUT_HEADER_ITEM_UNIQUE_ITEMS_MODIFY);
        diffStringList(newItem.getEnumValues(), origItem.getEnumValues(), ChangeType.OUTPUT_HEADER_ITEM_ENUM_VALUES_MODIFY);
        diffBigDecimal(newItem.getMultipleOf(),origItem.getMultipleOf(),ChangeType.OUTPUT_HEADER_ITEM_MULTIPLE_OF_MODIFY);
    }

    private void diffSchema(Schema newSchema, Schema origSchema, SchemaType schemaType) {
        // 通过ref判断是否为引用，如果是，从definition中获取到对应到数据，接着对Definition中的Schema进行递归对比
        if (StringUtils.isNotBlank(newSchema.getRef()) && StringUtils.isNotBlank(origSchema.getRef())) {
            Schema newRefSchema = null;
            Schema origRefSchema = null;
            for (Definition definition : newDefinitionList) {
                if (StringUtils.equals(definition.getName(),newSchema.getRef())) {
                    newRefSchema = definition.getSchema();
                    break;
                }
            }
            for (Definition definition : origDefinitionList) {
                if (StringUtils.equals(definition.getName(),origSchema.getRef())) {
                    origRefSchema = definition.getSchema();
                    break;
                }
            }
            this.diffString(newSchema.getRef(),origRefSchema.getRef(),ChangeType.OUTPUT_PARAMETER_NAME_MODIFY);
            this.diffSchema(newRefSchema,origRefSchema,schemaType);
        }

        this.diffString(newSchema.getFormat(), origSchema.getFormat(), ChangeType.OUTPUT_HEADER_MULTIPLE_OF_MODIFY);

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

    private void diffBigDecimal(BigDecimal newValue, BigDecimal origValue, ChangeType changeType) {
        if (newValue == null && origValue != null) {
            this.buildDiffResult(currentDiffPath, origValue.toString(), null, null, changeType, null);
        } else if (newValue != null && origValue == null) {
            this.buildDiffResult(currentDiffPath, null, newValue.toString(), null, changeType, null);
        } else if (newValue != null){
            if (newValue.compareTo(origValue) != 0) {
                this.buildDiffResult(currentDiffPath, origValue.toString(), newValue.toString(), null, changeType, null);
            }
        }
    }

    private void diffBoolean(Boolean newValue, Boolean origValue, ChangeType changeType) {
        if (newValue == null && origValue != null) {
            this.buildDiffResult(origValue + "", null, changeType);
        } else if (newValue != null && origValue == null) {
            this.buildDiffResult(null, newValue + "", changeType);
        } else if (newValue != null) {
            this.buildDiffResult(origValue + "", newValue + "", changeType);
        }
    }

    private void diffInteger(Integer newValue, Integer origValue, ChangeType changeType) {
        if (newValue == null && origValue != null) {
            this.buildDiffResult(origValue + "", null, changeType);
        } else if(newValue != null && origValue == null) {
            this.buildDiffResult(null,newValue + "",changeType);
        } else if (newValue != null) {
            this.buildDiffResult(origValue + "", newValue + "",changeType);
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


    private void buildDiffResult(String origValue, String newValue, ChangeType changeType) {
        this.buildDiffResult(currentDiffPath,origValue,newValue,null,changeType,null);
    }

}
