package com.niepengfei.mybatis.service;

import com.niepengfei.mybatis.mapper.RoleMapper;
import com.niepengfei.mybatis.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 2018-2020
 * <p>
 * user service
 * </p>
 *
 * @author Jack
 * @version 1.0.0
 * @since 1/3/2020
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    public Map<String,String> getUserById(Long id){
        return userMapper.getUserById(id);
    }
}
