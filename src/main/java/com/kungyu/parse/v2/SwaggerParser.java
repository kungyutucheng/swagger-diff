package com.kungyu.parse.v2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kungyu.BusinessException;
import com.kungyu.model.v2.Path;

import java.util.List;

/**
 * @author wengyongcheng
 * @since 2020/6/30 4:00 下午
 */
public class SwaggerParser {

    public List<Path> parse(String jsonStr) {
        JSONObject swagger = JSON.parseObject(jsonStr);
        if (!swagger.containsKey("swagger")) {
            throw new BusinessException("URL错误，非swagger2.0版本");
        }

        return null;
    }
}
