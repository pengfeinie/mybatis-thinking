package com.niepengfei.mybatis;

import com.niepengfei.mybatis.custom.NpfMybatisSqlSession;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class NpfMapperFactoryBean<T> implements FactoryBean {

    private Class<T> mapperInterface;

    public Object getObject() throws Exception {
        NpfMybatisSqlSession npfMybatisSqlSession = new NpfMybatisSqlSession();
        Object mapper = npfMybatisSqlSession.getMapper(mapperInterface);
        return mapper;
    }

    public Class<?> getObjectType() {
        return mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }
}
