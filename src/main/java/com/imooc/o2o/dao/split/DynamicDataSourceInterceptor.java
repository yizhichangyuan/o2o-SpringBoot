package com.imooc.o2o.dao.split;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;

/**
 * 拦截器拦截mybatis传递进来的SQL信息，根据SQL信息来使用相应的读写分离的数据源
 */
// mybatis会把增删改的方法封装在update这个method中
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DynamicDataSourceInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);
    private static final String REGEX = ".*insert\\u0020.*|.*update\\u0020.*|.*delete\\u0020.*";

    /**
     * 遇到什么情况进行拦截
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 利用spring提供的，判断当前是不是事务已表明是不是数据库操作
        boolean synchronizationActive = TransactionSynchronizationManager.isActualTransactionActive();
        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement) objects[0]; // 参数第一个是解释什么数据库操作
        String lookupKey = DynamicDataSourceHolder.DB_MASTER;
        if (synchronizationActive != true) {
            // 读方法，
            if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                // selectKey 为自增id查询主键（SELECT LAST_INSERT_ID())方法，使用主库，通常使用自增主键插入数据库操作
                if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    lookupKey = DynamicDataSourceHolder.DB_MASTER;
                } else {
                    BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", "");
                    if (sql.matches(REGEX)) {
                        lookupKey = DynamicDataSourceHolder.DB_MASTER;
                    } else {
                        lookupKey = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }
        } else {
            // 因为一般事务操作可能包含增删改查混合操作，所以用master
            lookupKey = DynamicDataSourceHolder.DB_MASTER;
        }
        logger.debug("设置方法[{}] use[{}] Strategy,SqlCommandType[{}]...", ms.getId(), lookupKey, ms.getSqlCommandType().name());
        DynamicDataSourceHolder.setDbType(lookupKey);
        return invocation.proceed();
    }

    /**
     * 返回封装好的代理对象，决定返回的是本体还是编织好的代理
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        // 这里的Executor是mybatis支持一系列增删改查操作的执行器
        if (target instanceof Executor) {
            // 如果是mybatis的Executor类型，那么就把
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * 非必要
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {

    }
}
