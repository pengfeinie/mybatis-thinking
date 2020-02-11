package com.niepengfei.mybatis.meta;

import lombok.Data;
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
@Data
public class User implements Serializable {

    private Long id;

    private String name;
}
