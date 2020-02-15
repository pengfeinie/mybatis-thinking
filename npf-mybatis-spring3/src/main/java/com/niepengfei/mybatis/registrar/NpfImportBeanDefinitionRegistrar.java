package com.niepengfei.mybatis.registrar;

import com.niepengfei.mybatis.factory.NpfMapperFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import java.util.Objects;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class NpfImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar,ResourceLoaderAware {

    private ResourcePatternResolver resolver;

    private MetadataReaderFactory metadataReaderFactory;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(NpfMapperScan.class.getName()));
        if (Objects.nonNull(annotationAttributes)) {
            String value = SystemPropertyUtils.resolvePlaceholders(annotationAttributes.getString("value"));
            String concat = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX.concat(ClassUtils.convertClassNameToResourcePath(value));
            String packageSearchPath = concat.concat("/**/*.class");
            try {
                Resource[] resources = resolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader  metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        if (metadataReader.getClassMetadata().isInterface()) {
                            Class<?> mapperClass = Class.forName(metadataReader.getClassMetadata().getClassName());
                            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(NpfMapperFactoryBean.class);
                            AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                            beanDefinition.getPropertyValues().add("mapperInterface",mapperClass);
                            registry.registerBeanDefinition(mapperClass.getSimpleName(),beanDefinition);
                        }
                    }
                }
            } catch (Exception e){
            }
        }
    }
}
