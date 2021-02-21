package com.imooc.o2o.service;

public interface CacheService {
    /**
     * 将所有key前缀为prefix的key从redis缓存中清空，此举是为了应对当发生对数据库中的修改时，
     * 及时将缓存中清空，下一次请求就会放进缓存中最新的
     *
     * @param prefix
     */
    void removeKeys(String prefix);
}
