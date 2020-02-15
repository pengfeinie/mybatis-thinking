package com.niepengfei.mybatis;

import com.niepengfei.mybatis.factory.NpfMapperFactoryBean;
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
    public NpfMapperFactoryBean<UserMapper> userMapper() {
        NpfMapperFactoryBean<UserMapper> userMapperNpfMapperFactoryBean = new NpfMapperFactoryBean<>();
        userMapperNpfMapperFactoryBean.setMapperInterface(UserMapper.class);
        return userMapperNpfMapperFactoryBean;
    }

    @Bean
    public NpfMapperFactoryBean<RoleMapper> roleMapper(){
        NpfMapperFactoryBean<RoleMapper> roleMapperNpfMapperFactoryBean = new NpfMapperFactoryBean<>();
        roleMapperNpfMapperFactoryBean.setMapperInterface(RoleMapper.class);
        return roleMapperNpfMapperFactoryBean;
    }
}
