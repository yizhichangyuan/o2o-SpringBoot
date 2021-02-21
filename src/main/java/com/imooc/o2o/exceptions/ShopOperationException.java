package com.imooc.o2o.exceptions;

/**
 * 自定义的exception需要继承RuntimeException，而不是Exception，因为@Transactional默认捕捉RuntimeException才会进行回滚
 */
public class ShopOperationException extends RuntimeException {
    public ShopOperationException(String msg) {
        super(msg);
    }
}
