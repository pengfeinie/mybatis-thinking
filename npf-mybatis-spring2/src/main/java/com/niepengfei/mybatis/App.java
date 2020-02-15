package com.niepengfei.mybatis;

import com.niepengfei.mybatis.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.util.Map;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService userService = (UserService)ac.getBean("userService");
        Map<String, String> map = userService.getUserById(2L);
        System.out.println(map);
    }
}
