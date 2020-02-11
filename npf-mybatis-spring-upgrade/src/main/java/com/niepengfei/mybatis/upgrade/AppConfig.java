package com.niepengfei.mybatis.upgrade;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
@ComponentScan("com.niepengfei.mybatis.upgrade")
@Configuration
@NpfMybatisMapperScan(value = "com.niepengfei.mybatis.upgrade.mapper")
public class AppConfig {

}
