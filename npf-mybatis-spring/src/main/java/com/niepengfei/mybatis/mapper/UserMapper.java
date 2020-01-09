package com.niepengfei.mybatis.mapper;

import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * <p>
 * user mapper
 * </p>
 *
 * @author Jack
 * @version 1.0.0
 * @since 1/3/2020
 */
public interface UserMapper {

    @Select(value = "select * from npf_user where id = #{id}")
    Map<String,String> getUserById(Long id);

}
