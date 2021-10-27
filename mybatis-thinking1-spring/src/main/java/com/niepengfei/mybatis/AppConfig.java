package com.niepengfei.mybatis;

import com.niepengfei.mybatis.factory.RoleMapperFactoryBean;
import com.niepengfei.mybatis.factory.UserMapperFactoryBean;
import com.niepengfei.mybatis.mapper.RoleMapper;
import com.niepengfei.mybatis.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
@ComponentScan("com.niepengfei.mybatis")
@Configuration
public class AppConfig {

    @Bean
    public UserMapperFactoryBean<UserMapper> userMapper() {
        return new UserMapperFactoryBean<UserMapper>();
    }

    @Bean
    public RoleMapperFactoryBean<RoleMapper> roleMapper(){
        return new RoleMapperFactoryBean<RoleMapper>();
    }
}
