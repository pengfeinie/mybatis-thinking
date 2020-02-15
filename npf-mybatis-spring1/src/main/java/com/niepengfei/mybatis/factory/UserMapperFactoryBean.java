package com.niepengfei.mybatis.factory;

import com.niepengfei.mybatis.mapper.UserMapper;
import com.niepengfei.mybatis.session.NpfMybatisSqlSession;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class UserMapperFactoryBean<T> implements FactoryBean<T> {

    public T getObject() throws Exception {
        return NpfMybatisSqlSession.getMapper(UserMapper.class);
    }

    public Class<?> getObjectType() {
        return UserMapper.class;
    }
}
