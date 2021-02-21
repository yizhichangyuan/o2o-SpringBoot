package com.imooc.o2o.config.redis;

import com.imooc.o2o.cache.JedisPoolWriper;
import com.imooc.o2o.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfiguration {
//    @Value("${redis.pool.maxActive}")
//    private int maxActive;
//    @Value("${redis.hostname}")
//    private String hostname;
//    @Value("${redis.port}")
//    private int port;
//    @Value("${redis.pool.maxIdle}")
//    private int maxIdle;
//    @Value("${redis.pool.maxWait}")
//    private int maxWait;
//    @Value("${redis.pool.testOnBorrow}")
//    private boolean testOnBorrow;
//
//    @Autowired
//    private JedisPoolConfig jedisPoolConfig;
//    @Autowired
//    private JedisPoolWriper jedisWritePool;
//    @Autowired
//    private JedisUtil jedisUtil;
//
//    /**
//     * redis连接池配置
//     * @return
//     */
//    @Bean("jedisPoolConfig")
//    public JedisPoolConfig createJedisPoolConfig() {
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        // 控制一个pool可以分配多少个jedis实例
//        jedisPoolConfig.setMaxTotal(maxActive);
//        // 连接池最多空闲多少个连接
//        // 表示解释没有数据库连接时依然保持的链接数目，而不被清除，随时处于待命状态
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        // 最大等待时间：但没有可用连接时，连接池等待连接被归还的最大时间，超出时间则抛出异常
//        jedisPoolConfig.setMaxWaitMillis(maxWait);
//        // 在获取连接时检查有效性
//        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
//        return jedisPoolConfig;
//    }
//
//    /**
//     * 创建Redis连接池，并做相关配置
//     * @return
//     */
//    @Bean("jedisWritePool")
//    public JedisPoolWriper createJedisPoolWriper(){
//        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
//        return jedisPoolWriper;
//    }
//
//    /**
//     * 创建Redis工具类，封装好redis的连接已进行相关操作
//     * @return
//     */
//    @Bean("jedisUtil")
//    public JedisUtil createJedisUtil(){
//        JedisUtil jedisUtil = new JedisUtil();
//        jedisUtil.setJedisPool(jedisWritePool);
//        return jedisUtil;
//    }
//
//    /**
//     * jedis的key操作
//     * @return
//     */
//    @Bean("jedisKeys")
//    public JedisUtil.Keys createKeys(){
//        // 注意内部类的new方式
//        JedisUtil.Keys keys = jedisUtil.new Keys();
//        return keys;
//    }
//
//    /**
//     * jedis的Strings操作
//     * @return
//     */
//    @Bean("jedisStrings")
//    public JedisUtil.Strings createStrings(){
//        JedisUtil.Strings strings = jedisUtil.new Strings();
//        return strings;
//    }
}
