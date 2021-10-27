package com.niepengfei.mybatis.registrar;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class NpfImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar{

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(NpfMapperScan.class.getName()));
        if (Objects.nonNull(annotationAttributes)) {
            registerBeanDefinitions(annotationAttributes, registry, generateBaseBeanName(importingClassMetadata));
        }
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata) {
        return importingClassMetadata.getClassName() + "#" + NpfImportBeanDefinitionRegistrar.class.getSimpleName() + "#" + 0;
    }

    private static void registerBeanDefinitions(AnnotationAttributes annotationAttributes, BeanDefinitionRegistry registry, String beanName) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NpfMapperScanBeanDefinitionRegistryPostProcessor.class);
        List<String> basePackages = new ArrayList<>();
        basePackages.addAll(Arrays.stream(annotationAttributes.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
        basePackages.addAll(Arrays.stream(annotationAttributes.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }
}
