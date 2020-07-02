package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import org.jetbrains.annotations.NotNull;

/**
 * @author wengyongcheng
 * @since 2020/6/30 11:38 上午
 */
public class Xml {

    private String name;

    private String namespace;

    private String prefix;

    private Boolean attribute;

    private Boolean wrapped;

    public static Xml convertToXml(JSONObject xmlJson) {
        return new XmlConverter().doBackward(xmlJson);
    }

    private static final class XmlConverter extends Converter<Xml, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Xml xml) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Xml doBackward(@NotNull JSONObject xmlJson) {
            Xml xml = new Xml();
            xml.setName(xmlJson.getString("name"));
            xml.setNamespace(xmlJson.getString("namespace"));
            xml.setPrefix(xmlJson.getString("prefix"));
            xml.setAttribute(xmlJson.getBoolean("attribute"));
            xml.setWrapped(xmlJson.getBoolean("wrapped"));
            return xml;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getAttribute() {
        return attribute;
    }

    public void setAttribute(Boolean attribute) {
        this.attribute = attribute;
    }

    public Boolean getWrapped() {
        return wrapped;
    }

    public void setWrapped(Boolean wrapped) {
        this.wrapped = wrapped;
    }
}
