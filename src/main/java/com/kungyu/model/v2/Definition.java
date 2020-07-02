package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import com.kungyu.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Set;

/**
 * @author wengyongcheng
 * @since 2020/7/2 5:14 下午
 */
public class Definition {

    private String name;

    private Schema schema;

    public static Definition convertToDefinition(@NotNull JSONObject definitionJson) {
        return new DefinitionConverter().doBackward(definitionJson);
    }

    private static final class DefinitionConverter extends Converter<Definition, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Definition definition) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Definition doBackward(@NotNull JSONObject definitionJson) {
            Set<String> keySet = definitionJson.keySet();
            if (CollectionUtils.isEmpty(keySet) || keySet.size() > 1) {
                throw new BusinessException("Definition json格式不合法");
            }
            Iterator<String> iterator = keySet.iterator();
            Definition definition = new Definition();
            definition.setName(iterator.next());
            JSONObject schemaJson = definitionJson.getJSONObject(definition.getName());
            if (schemaJson != null) {
                definition.setSchema(Schema.convertToSchema(schemaJson));
            }
            return definition;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }
}
