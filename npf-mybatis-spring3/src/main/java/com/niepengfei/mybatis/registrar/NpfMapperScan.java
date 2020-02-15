package com.niepengfei.mybatis.registrar;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
@Retention(RetentionPolicy.RUNTIME)
@Import(NpfImportBeanDefinitionRegistrar.class)
public @interface NpfMapperScan {

    String value();
}
