package com.niepengfei.mybatis.upgrade;


import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(NpfMybatisMapperScannerImportBeanDefinitionRegistrar.class)
public @interface NpfMybatisMapperScan {

    String value();

    String basePackage() default "";

    String[] basePackages() default {};

    /**
     * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring bean.
     *
     * @return the class of {@code MapperFactoryBean}
     */
    Class<? extends MapperFactoryBean> factoryBean() default MapperFactoryBean.class;
}
