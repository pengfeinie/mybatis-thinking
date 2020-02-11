package com.niepengfei.mybatis.upgrade;

import com.niepengfei.mybatis.upgrade.mapper.UserMapper;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(AppConfig.class);
        UserMapper userMapper = ac.getBean(UserMapper.class);
        System.out.println(userMapper);
    }
}
