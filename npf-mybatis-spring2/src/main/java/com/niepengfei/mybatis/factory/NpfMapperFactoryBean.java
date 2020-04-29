package com.niepengfei.mybatis.factory;

import com.niepengfei.mybatis.session.NpfMybatisSqlSession;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class NpfMapperFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;

    @Override
    public T getObject() throws Exception {
        return NpfMybatisSqlSession.getMapper(mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }
}
