package com.zd.interceptor;

import com.zd.util.Oauth2Util;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库拦截器-设置数据库的公共字段
 *
 * @author Zidong
 * @date 2019/5/5 9:04 AM
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class CommonFieldInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String FIELD_CAMELCASE_CREATE_TIME = "createTime";
    private static final String FIELD_UNDERLINE_CREATE_TIME = "create_time";

    private static final String FIELD_CAMELCASE_UPDATE_TIME = "updateTime";
    private static final String FIELD_UNDERLINE_UPDATE_TIME = "update_time";

    private static final String FIELD_CAMELCASE_CREATE_BY = "createBy";
    private static final String FIELD_UNDERLINE_CREATE_BY = "create_by";
    private static final String FIELD_UNDERLINE_CREATE_BY2 = "create_user";
    private static final String FIELD_UNDERLINE_CREATE_BY3 = "createUser";

    private static final String FIELD_CAMELCASE_UPDATE_BY = "updateBy";
    private static final String FIELD_UNDERLINE_UPDATE_BY = "update_by";
    private static final String FIELD_UNDERLINE_UPDATE_BY2 = "update_user";
    private static final String FIELD_UNDERLINE_UPDATE_BY3 = "updateUser";


    @SuppressWarnings({"rawtypes"})
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Object parameter = null;
            try {
                // 如果获取到 2个参数，在第二个参数中仍有两个参数，第二个参数中的 "param1"中才有参数列表
                parameter = invocation.getArgs()[1];
            } catch (Exception e) {
                logger.error("公共字段拦截器发生异常，异常信息为：{}", e.getMessage());
            }
            if (parameter == null) {
                // 不是需要被攔截的類型, 不做操作
                return invocation.proceed();
            }
            Date time = new Date();
            Class classParameter = parameter.getClass();
            Field[] fields = classParameter.getDeclaredFields();
            // 兼容处理
            if (fields.length == 1) {
                Map paramMap = (Map) parameter;
                parameter = paramMap.get("param1");
                fields = parameter.getClass().getDeclaredFields();
            }
            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            // 拿到每一个字段，依次判断是否为目标字段（createBy...updateTime...）
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (FIELD_CAMELCASE_CREATE_TIME.equalsIgnoreCase(fieldName) || FIELD_UNDERLINE_CREATE_TIME.equalsIgnoreCase(fieldName)) {
                    Object value = field.get(parameter);
                    // 仅当此字段未被设置的时候才会执行修改
                    if (value == null) {
                        // create_time只有插入才添加
                        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                            field.set(parameter, time);
                        }
                    }
                } else if (FIELD_CAMELCASE_UPDATE_TIME.equalsIgnoreCase(fieldName) || FIELD_UNDERLINE_UPDATE_TIME.equalsIgnoreCase(fieldName)) {
                    Object value = field.get(parameter);
                    if (value == null) {
                        field.set(parameter, time);
                    }
                }
                if (FIELD_CAMELCASE_CREATE_BY.equalsIgnoreCase(fieldName) || FIELD_UNDERLINE_CREATE_BY.equalsIgnoreCase(fieldName) || FIELD_UNDERLINE_CREATE_BY2.equalsIgnoreCase(fieldName) || FIELD_UNDERLINE_CREATE_BY3.equalsIgnoreCase(fieldName)) {
                    Object value = field.get(parameter);
                    if (value == null) {
                        // createBy只有插入才修改
                        if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                            modifyUserFiele(parameter, field);
                        }
                    }
                } else if (FIELD_CAMELCASE_UPDATE_BY.equalsIgnoreCase(fieldName) || FIELD_UNDERLINE_UPDATE_BY.equalsIgnoreCase(fieldName) || FIELD_UNDERLINE_UPDATE_BY2.equalsIgnoreCase(fieldName) || FIELD_UNDERLINE_UPDATE_BY3.equalsIgnoreCase(fieldName)) {
                    Object value = field.get(parameter);
                    if (value == null) {
                        modifyUserFiele(parameter, field);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("设置数据库公共字段时出错", e);
        }
        return invocation.proceed();
    }

    /**
     * 修改参数
     *
     * @param parameter 字段信息
     * @param field
     * @throws IllegalAccessException
     */
    private void modifyUserFiele(Object parameter, Field field) throws IllegalAccessException {
        try {
            field.set(parameter, Oauth2Util.getUserId());
        } catch (Exception e) {
            field.set(parameter, "UNAUTH");
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}