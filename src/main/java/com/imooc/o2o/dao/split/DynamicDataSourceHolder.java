package com.imooc.o2o.dao.split;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 池子，放入决定数据源key的池子，其中池子利用ThreadLocal设置为线程安全
 */
public class DynamicDataSourceHolder {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceHolder.class);
    // 池子，利用ThreadLocal保证线程安全，线程安全是访问同一个变量时容器出现问题，特别多个线程对一个变量进行写入的时候
    // ThreadLocal是除了加锁之外的另外一种保证多线程访问变量的线程安全方法，即每个线程对变量的访问都是基于线程自己的变量
    // 也就是说中央变量只有一个，但是每个线程都有一个中央变量的独立拷贝，每个线程只访问修改自己独立拷贝的变量
    private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static String DB_MASTER = "master";
    public static String DB_SLAVE = "slave";

    public static String getDbType() {
        String db = contextHolder.get();
        if (db == null) {
            db = DB_MASTER;
        }
        return db;
    }

    /**
     * 设置线程的dbType
     *
     * @param str
     */
    public static void setDbType(String str) {
        logger.debug("所使用的数据源为：" + str);
        contextHolder.set(str);
    }

    /**
     * 清理连接类型
     */
    public static void clearDbType() {
        contextHolder.remove();
    }


}
