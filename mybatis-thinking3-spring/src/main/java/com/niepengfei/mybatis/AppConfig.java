package com.niepengfei.mybatis;

import com.niepengfei.mybatis.registrar.NpfMapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
@ComponentScan("com.niepengfei.mybatis")
@Configuration
@NpfMapperScan("com.niepengfei.mybatis.mapper")
public class AppConfig {


}
