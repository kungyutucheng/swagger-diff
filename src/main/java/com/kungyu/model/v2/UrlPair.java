package com.kungyu.model.v2;


import java.io.Serializable;

/**
 * @author wengyongcheng
 * @since 2020/6/28 12:01 下午
 */
public class UrlPair implements Serializable {


    private static final long serialVersionUID = 3081217581317925604L;
    private String newUrl;

    private String oldUrl;

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getOldUrl() {
        return oldUrl;
    }

    public void setOldUrl(String oldUrl) {
        this.oldUrl = oldUrl;
    }
}
