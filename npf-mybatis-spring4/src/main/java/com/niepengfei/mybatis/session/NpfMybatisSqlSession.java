package com.niepengfei.mybatis.session;

import com.niepengfei.mybatis.proxy.NpfMapperProxy;

import java.lang.reflect.Proxy;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class NpfMybatisSqlSession {

    public static <T> T  getMapper(Class<?> interfaceClazz){
        return (T)Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class[]{interfaceClazz},new NpfMapperProxy());
    }
}
