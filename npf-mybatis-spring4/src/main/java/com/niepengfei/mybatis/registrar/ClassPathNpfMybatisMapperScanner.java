
package com.niepengfei.mybatis.registrar;

import com.niepengfei.mybatis.factory.NpfMapperFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import java.util.Set;


/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class ClassPathNpfMybatisMapperScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends NpfMapperFactoryBean> mapperFactoryBeanClass = NpfMapperFactoryBean.class;

    public ClassPathNpfMybatisMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setMapperFactoryBeanClass(Class<? extends NpfMapperFactoryBean<?>> mapperFactoryBeanClass) {
        this.mapperFactoryBeanClass = mapperFactoryBeanClass == null ? NpfMapperFactoryBean.class : mapperFactoryBeanClass;
    }

    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
       Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
       for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is NpfMapperFactoryBean
            definition.setBeanClass(this.mapperFactoryBeanClass);
            definition.getPropertyValues().add("mapperInterface",beanClassName);
       }
       return beanDefinitions;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
      return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}
