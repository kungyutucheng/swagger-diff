package com.kungyu.model;

import com.kungyu.enums.Format;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wengyongcheng
 * @since 2020/6/30 2:55 下午
 */
public class Items {

    private String type;

    private Format format;

    private Items items;

    private String collectionFormat;

    private String defaultValue;

    private BigDecimal maximum;

    private BigDecimal exclusiveMaximum;

    private BigDecimal minimum;

    private BigDecimal exclusiveMinimum;

    private Integer maxLength;

    private Integer minLength;

    private String pattern;

    private Integer maxItems;

    private Integer minItems;

    private Boolean uniqueItems;

    private List<String> enumValues;

    private BigDecimal multipleOf;

}
