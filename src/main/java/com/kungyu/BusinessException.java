package com.kungyu;

/**
 * @author wengyongcheng
 * @since 2020/6/30 4:09 下午
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
