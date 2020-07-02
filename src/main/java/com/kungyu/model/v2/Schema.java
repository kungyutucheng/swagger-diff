package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import com.kungyu.constant.CommonConstant;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wengyongcheng
 * @since 2020/6/30 11:16 上午
 */
public class Schema {

    private String ref;

    private String format;

    private String title;

    private String description;

    private String defaultValue;

    private BigDecimal multipleOf;

    private BigDecimal maximum;

    private Boolean exclusiveMaximum;

    private BigDecimal minimum;

    private Boolean exclusiveMinimum;

    private Integer maxLength;

    private Integer minLength;

    private String pattern;

    private Integer maxItems;

    private Integer minItems;

    private Boolean uniqueItems;

    private Integer maxProperties;

    private Integer minProperties;

    private Boolean required;

    private List<String> enumValues;

    private String type;

    private Schema items;

    private Schema allOf;

    private Map<String, Schema> properties;

    private Schema additionalProperties;

    private String discriminator;

    private Boolean readOnly;

    private Xml xml;

    private ExternalDocumentation externalDocumentation;

    private String example;

    public static Schema convertToSchema(JSONObject schemaJson) {
        return new SchemaConverter().doBackward(schemaJson);
    }

    private static final class SchemaConverter extends Converter<Schema, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Schema schema) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Schema doBackward(@NotNull JSONObject schemaJson) {
            Schema schema = new Schema();

            schema.setRef(schemaJson.getString("ref"));
            schema.setTitle(schemaJson.getString("title"));
            schema.setFormat(schemaJson.getString("format"));
            schema.setDescription(schemaJson.getString("description"));
            schema.setDefaultValue(schemaJson.getString("defaultValue"));

            schema.setMultipleOf(schemaJson.getBigDecimal("multipleOf"));
            schema.setMaximum(schemaJson.getBigDecimal("maximum"));
            schema.setExclusiveMaximum(schemaJson.getBoolean("exclusiveMaximum"));
            schema.setMinimum(schemaJson.getBigDecimal("minimum"));
            schema.setExclusiveMinimum(schemaJson.getBoolean("exclusiveMinimum"));
            schema.setMaxLength(schemaJson.getInteger("maxLength"));
            schema.setMinLength(schemaJson.getInteger("minLength"));
            schema.setPattern(schemaJson.getString("pattern"));
            schema.setMaxItems(schemaJson.getInteger("maxItems"));
            schema.setMinItems(schemaJson.getInteger("minItems"));
            schema.setUniqueItems(schemaJson.getBoolean("uniqueItems"));
            schema.setMaxProperties(schemaJson.getInteger("maxProperties"));
            schema.setMinProperties(schemaJson.getInteger("minProperties"));
            schema.setRequired(schemaJson.getBoolean("required"));

            JSONArray enumValueArray = schemaJson.getJSONArray("enum");
            if (enumValueArray != null && enumValueArray.size() > 0) {
                schema.setEnumValues(enumValueArray.toJavaList(String.class));
            }

            schema.setType(schemaJson.getString("type"));

            JSONObject itemJson = schemaJson.getJSONObject("items");
            if (itemJson != null) {
                schema.setItems(Schema.convertToSchema(itemJson));
            }

            JSONObject allOfJson = schemaJson.getJSONObject("allOf");
            if (allOfJson != null) {
                schema.setAllOf(Schema.convertToSchema(allOfJson));
            }

            JSONObject propertiesJson = schemaJson.getJSONObject("properties");
            if (propertiesJson != null) {
                Set<String> propertyNameSet = propertiesJson.keySet();
                Map<String, Schema> properties = new HashMap<>(propertyNameSet.size());
                for (String propertyName : propertyNameSet) {
                    properties.put(propertyName, Schema.convertToSchema(propertiesJson.getJSONObject(propertyName)));
                }
                schema.setProperties(properties);
            }

            JSONObject additionalProperties = schemaJson.getJSONObject("additionalProperties");
            if (additionalProperties != null) {
                schema.setAdditionalProperties(Schema.convertToSchema(additionalProperties));
            }

            schema.setDiscriminator(schemaJson.getString("discriminator"));
            schema.setReadOnly(schemaJson.getBoolean("readOnly"));

            JSONObject xmlJson = schemaJson.getJSONObject("xml");
            if (xmlJson != null) {
                schema.setXml(Xml.convertToXml(xmlJson));
            }

            JSONObject externalDocumentationJson = schemaJson.getJSONObject("externalDocumentation");
            if (externalDocumentationJson != null) {
                schema.setExternalDocumentation(ExternalDocumentation.convertToExternalDocumentation(externalDocumentationJson));
            }

            schema.setExample(schemaJson.getString("example"));

            return schema;
        }
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public BigDecimal getMultipleOf() {
        return multipleOf;
    }

    public void setMultipleOf(BigDecimal multipleOf) {
        this.multipleOf = multipleOf;
    }

    public BigDecimal getMaximum() {
        return maximum;
    }

    public void setMaximum(BigDecimal maximum) {
        this.maximum = maximum;
    }

    public Boolean getExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public void setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
    }

    public BigDecimal getMinimum() {
        return minimum;
    }

    public void setMinimum(BigDecimal minimum) {
        this.minimum = minimum;
    }

    public Boolean getExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public void setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(Integer maxItems) {
        this.maxItems = maxItems;
    }

    public Integer getMinItems() {
        return minItems;
    }

    public void setMinItems(Integer minItems) {
        this.minItems = minItems;
    }

    public Boolean getUniqueItems() {
        return uniqueItems;
    }

    public void setUniqueItems(Boolean uniqueItems) {
        this.uniqueItems = uniqueItems;
    }

    public Integer getMaxProperties() {
        return maxProperties;
    }

    public void setMaxProperties(Integer maxProperties) {
        this.maxProperties = maxProperties;
    }

    public Integer getMinProperties() {
        return minProperties;
    }

    public void setMinProperties(Integer minProperties) {
        this.minProperties = minProperties;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Schema getItems() {
        return items;
    }

    public void setItems(Schema items) {
        this.items = items;
    }

    public Schema getAllOf() {
        return allOf;
    }

    public void setAllOf(Schema allOf) {
        this.allOf = allOf;
    }

    public Map<String, Schema> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Schema> properties) {
        this.properties = properties;
    }

    public Schema getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Schema additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Xml getXml() {
        return xml;
    }

    public void setXml(Xml xml) {
        this.xml = xml;
    }

    public ExternalDocumentation getExternalDocumentation() {
        return externalDocumentation;
    }

    public void setExternalDocumentation(ExternalDocumentation externalDocumentation) {
        this.externalDocumentation = externalDocumentation;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
