package com.niepengfei.mybatis;

import com.niepengfei.mybatis.mapper.UserMapper;
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

import java.util.Set;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class NpfImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar,ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private ResourcePatternResolver resolver;

    private MetadataReaderFactory metadataReaderFactory;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.
                fromMap(importingClassMetadata.getAnnotationAttributes(NpfMapperScan.class.getName()));
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    .concat(ClassUtils.convertClassNameToResourcePath(
                            SystemPropertyUtils.resolvePlaceholders(annotationAttributes.getString("value")))
                            .concat("/**/*.class"));
            try {
                Resource[] resources = resolver.getResources(packageSearchPath);
                MetadataReader metadataReader = null;
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        metadataReader = metadataReaderFactory.getMetadataReader(resource);
                        try {
                            if (!metadataReader.getClassMetadata().isConcrete()) {
                                Class<?> mapperClass = Class.forName(metadataReader.getClassMetadata().getClassName());
                                BeanDefinitionBuilder beanDefinitionBuilder =
                                        BeanDefinitionBuilder.genericBeanDefinition(NpfMapperFactoryBean.class);
                                AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                                beanDefinition.getPropertyValues().add("mapperInterface",mapperClass);
                                registry.registerBeanDefinition(mapperClass.getSimpleName(),beanDefinition);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e){

            }
    }
}
