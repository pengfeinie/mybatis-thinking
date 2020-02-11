
package com.niepengfei.mybatis.upgrade;


import org.springframework.beans.factory.FactoryBean;
import java.lang.reflect.Proxy;

public class MapperFactoryBean<T> implements FactoryBean<T> {

  private Class<T> mapperInterface;

  public MapperFactoryBean(Class<T> mapperInterface) {
    this.mapperInterface = mapperInterface;
  }

  @Override
  public T getObject() throws Exception {
     return (T)Proxy.newProxyInstance(mapperInterface.getClassLoader(),
            new Class[]{mapperInterface},new NpfInvocationHandler());
  }

  @Override
  public Class<T> getObjectType() {
     return this.mapperInterface;
  }

  @Override
  public boolean isSingleton() {
     return true;
  }

}
