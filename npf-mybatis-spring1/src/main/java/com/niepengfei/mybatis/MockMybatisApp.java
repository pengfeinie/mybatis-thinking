package com.niepengfei.mybatis;

import com.niepengfei.mybatis.mapper.UserMapper;
import com.niepengfei.mybatis.session.NpfMybatisSqlSession;

import java.util.Map;

public class MockMybatisApp {

    public static void main(String[] args) {
        UserMapper userMapper = NpfMybatisSqlSession.getMapper(UserMapper.class);
        Map<String, String> userInfo = userMapper.getUserById(2L);
        System.out.println(userInfo);
    }
}
