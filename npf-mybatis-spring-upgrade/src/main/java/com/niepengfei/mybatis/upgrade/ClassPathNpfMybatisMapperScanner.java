
package com.niepengfei.mybatis.upgrade;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import java.util.Set;


public class ClassPathNpfMybatisMapperScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends MapperFactoryBean> mapperFactoryBeanClass = MapperFactoryBean.class;

    public ClassPathNpfMybatisMapperScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setMapperFactoryBeanClass(Class<? extends MapperFactoryBean> mapperFactoryBeanClass) {
        this.mapperFactoryBeanClass = mapperFactoryBeanClass == null ? MapperFactoryBean.class : mapperFactoryBeanClass;
    }

    /**
     * Calls the parent search that will search and register all the candidates.
     * Then the registered objects are post processed to set them as MapperFactoryBeans
     */
    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
      Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
      if (!beanDefinitions.isEmpty()) {
        processBeanDefinitions(beanDefinitions);
      }
      return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        for (BeanDefinitionHolder holder : beanDefinitions) {
            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            // the mapper interface is the original class of the bean
            // but, the actual class of the bean is MapperFactoryBean
            definition.setBeanClass(this.mapperFactoryBeanClass);
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
      return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    public void registerFilters() {
        addIncludeFilter((metadataReader, metadataReaderFactory) -> true);
    }
}
