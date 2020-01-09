package com.niepengfei.mybatis;

import com.niepengfei.mybatis.custom.NpfMybatisSqlSession;
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
@NpfMapperScan(value = "com.niepengfei.mybatis.mapper")
public class AppConfig {

//    @Bean
//    public UserMapper userMapper() throws Exception{
//        NpfMapperFactoryBean<UserMapper> factoryBean = new NpfMapperFactoryBean<UserMapper>();
//        factoryBean.setMapperInterface(UserMapper.class);
//        return (UserMapper)factoryBean.getObject();
//    }

//    @Bean
//    public UserMapper userMapper(){
//        NpfMybatisSqlSession npfMybatisSqlSession = new NpfMybatisSqlSession();
//        UserMapper mapper = npfMybatisSqlSession.getMapper(UserMapper.class);
//        return mapper;
//    }
}
