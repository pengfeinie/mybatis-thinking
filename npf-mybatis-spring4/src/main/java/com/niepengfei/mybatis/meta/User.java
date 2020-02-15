package com.niepengfei.mybatis.meta;

import java.io.Serializable;

/**
 * <p>
 *  User
 * </p>
 *
 * @author Jack
 * @version 1.0.0
 * @since 1/3/2020
 */
public class User implements Serializable {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
