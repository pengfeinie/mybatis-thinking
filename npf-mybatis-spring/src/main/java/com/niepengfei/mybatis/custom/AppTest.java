package com.niepengfei.mybatis.custom;

import com.niepengfei.mybatis.mapper.UserMapper;

import java.util.Map;

/**
 * @author niepengfei
 * @version 1.0.0
 * @since 2020/1/4
 */
public class AppTest {

    public static void main(String[] args) {
        NpfMybatisSqlSession npfMybatisSqlSession = new NpfMybatisSqlSession();
        UserMapper mapper = npfMybatisSqlSession.getMapper(UserMapper.class);
        Map<String, String> userMap = mapper.getUserById(2L);
        System.out.println(userMap);
    }
}
