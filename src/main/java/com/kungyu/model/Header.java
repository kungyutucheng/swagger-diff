package com.kungyu.model;

import com.kungyu.enums.Format;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wengyongcheng
 * @since 2020/6/30 3:06 下午
 */
public class Header {

    private String description;

    private String type;

    private Format format;

    private Items items;

    private String collectionFormat;

    private String defaultValue;

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

    private List<String> enumValues;

    private BigDecimal multipleOf;
}
