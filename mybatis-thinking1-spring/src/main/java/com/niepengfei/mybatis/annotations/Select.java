package com.niepengfei.mybatis.annotations;

import java.lang.annotation.*;

/**
 * @author Jack
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {

  String[] value();

}