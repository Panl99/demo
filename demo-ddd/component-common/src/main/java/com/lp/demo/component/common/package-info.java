@QueryEntities(value = {AbstractEntity.class})
package com.lp.demo.component.common;

import com.lp.demo.component.common.support.AbstractEntity;
import com.querydsl.core.annotations.QueryEntities;

/**
 * 引入 QueryDsl 有一个问题就是如果我们的父类在别的包里面，那么这个插件是默认不会找到父类并生成父类的 Q 类的，所以要在项目中加上一个 package-info 类。
 */