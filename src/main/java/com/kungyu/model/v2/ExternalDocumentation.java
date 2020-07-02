package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import org.jetbrains.annotations.NotNull;

/**
 * @author wengyongcheng
 * @since 2020/6/30 11:41 上午
 */
public class ExternalDocumentation {

    private String description;

    private String url;

    public static ExternalDocumentation convertToExternalDocumentation(@NotNull JSONObject externalDocumentationJson) {
        return new ExternalDocumentationConverter().doBackward(externalDocumentationJson);
    }

    private static final class ExternalDocumentationConverter extends Converter<ExternalDocumentation, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull ExternalDocumentation externalDocumentation) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected ExternalDocumentation doBackward(@NotNull JSONObject externalDocumentationJson) {
            ExternalDocumentation externalDocumentation = new ExternalDocumentation();
            externalDocumentation.setDescription(externalDocumentationJson.getString("description"));
            externalDocumentation.setUrl(externalDocumentationJson.getString("url"));
            return externalDocumentation;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
