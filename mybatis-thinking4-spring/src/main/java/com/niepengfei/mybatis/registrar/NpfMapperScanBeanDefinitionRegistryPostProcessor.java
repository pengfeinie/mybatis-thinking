package com.niepengfei.mybatis.registrar;

import com.niepengfei.mybatis.factory.NpfMapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;


/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class NpfMapperScanBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor,ApplicationContextAware, BeanNameAware {

    private String basePackage;

    private String beanName;

    private ApplicationContext applicationContext;

    private Class<? extends NpfMapperFactoryBean<?>> mapperFactoryBeanClass;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathNpfMybatisMapperScanner scanner = new ClassPathNpfMybatisMapperScanner(registry);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setMapperFactoryBeanClass(this.mapperFactoryBeanClass);
        scanner.addIncludeFilter(((metadataReader, metadataReaderFactory) -> true));
        scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // left intentionally blank
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    public void setMapperFactoryBeanClass(Class<? extends NpfMapperFactoryBean<?>> mapperFactoryBeanClass) {
        this.mapperFactoryBeanClass = mapperFactoryBeanClass;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
