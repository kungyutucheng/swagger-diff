package com.kungyu.model.v2;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Converter;
import com.kungyu.BusinessException;
import com.kungyu.enums.MethodType;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author wengyongcheng
 * @since 2020/6/28 6:07 下午
 */
public class Path {

    private String path;

    private Map<MethodType, Method> methodList;

    private String ref;


    public static Path convertToPath(JSONObject jsonObject) {
        PathConverter pathConverter = new PathConverter();
        return pathConverter.doBackward(jsonObject);
    }

    private static class PathConverter extends Converter<Path, JSONObject> {

        @Override
        protected JSONObject doForward(@NotNull Path path) {
            throw  new UnsupportedOperationException();
        }

        @Override
        protected Path doBackward(@NotNull JSONObject jsonObject) {
            Set<String> keySet = jsonObject.keySet();
            if (CollectionUtils.isEmpty(keySet) || keySet.size() > 1) {
                throw new BusinessException("解析path错误：未到解析到合适到key");
            }
            Iterator<String> iterator = keySet.iterator();
            // 获取到path路径
            // eg：/api/save
            String pathStr = iterator.next();

            Path path = new Path();
            path.setPath(pathStr);

            // 获取path下到method map
            // eg： {"/api/save":{"post",{}}}
            JSONObject pathJson = jsonObject.getJSONObject(pathStr);
            Set<String> methodKeySet = pathJson.keySet();
            if (CollectionUtils.isEmpty(methodKeySet)) {
                return path;
            }

            Map<MethodType, Method> methodList = new HashMap<>();
            for (String methodName : methodKeySet) {
                MethodType.getByName(methodName).ifPresent(methodType -> {
                    methodList.computeIfAbsent(methodType, (tmp) -> Method.convertToMethod(pathJson.getJSONObject(methodType.getName())));
                });
            }
            path.setMethodList(methodList);

            return path;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<MethodType, Method> getMethodList() {
        return methodList;
    }

    public void setMethodList(Map<MethodType, Method> methodList) {
        this.methodList = methodList;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public static void main(String[] args) {
        JSONObject methodListJSON = new JSONObject();
        JSONObject methodJSON = new JSONObject();
        methodJSON.put("description", "description");
        methodListJSON.put("post", methodJSON);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", methodListJSON);

        Path path = Path.convertToPath(jsonObject);

        System.out.println(path.getPath());
        System.out.println(path.getMethodList());

    }
}
