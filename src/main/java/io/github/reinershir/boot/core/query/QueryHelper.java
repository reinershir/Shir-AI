package io.github.reinershir.boot.core.query;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.hutool.core.bean.BeanUtil;
import io.github.reinershir.boot.core.query.annotation.QueryRule;
import io.github.reinershir.boot.core.query.annotation.QueryRuleEnum;
import io.github.reinershir.boot.model.User;
import io.github.reinershir.boot.utils.FieldUtils;

/**
* Copyright: Copyright (c) 2023 reiner
* @ClassName: QueryHelper.java
* @Description: 
* @version: v1.0.0
* @author: ReinerShir
* @date: 2023年7月30日 下午10:50:57 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2023年7月30日     ReinerShir   v1.0.0      
 */
public class QueryHelper {

	public static <T> QueryWrapper<T> initQueryWrapper(T entity){
		PropertyDescriptor[] properties = BeanUtil.getPropertyDescriptors(entity.getClass());
		QueryWrapper<T> wrapper =  new QueryWrapper<T>();
		for (PropertyDescriptor property : properties) {
			String name = property.getName();
			Object value=null;
			try {
				value = property.getReadMethod().invoke(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(value!=null&&!"".equals(value)) {
				Field filed=null;
				try {
					filed = entity.getClass().getDeclaredField(name);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(filed!=null) {
					QueryRule queryRule = filed.getAnnotation(QueryRule.class);
					String columnName = FieldUtils.camelToUnderline(name);
					if(queryRule!=null) {
						QueryRuleEnum rule = queryRule.value();
						switch(rule) {
						case GE:
							wrapper.ge(columnName, value);
							break;
						case LE:
							wrapper.le(columnName, value);
							break;
						case GT:
							wrapper.gt(columnName, value);
							break;
						case LT:
							wrapper.lt(columnName, value);
							break;
						case IN:
							wrapper.in(columnName, value);
							break;
						case LEFT_LIKE:
							wrapper.likeLeft(columnName, value);
							break;
						case RIGHT_LIKE:
							wrapper.likeRight(columnName, value);
							break;
						case LIKE:
							wrapper.like(columnName, value);
							break;
						case EMPTY:
							wrapper.isNull(columnName);
							break;
						case NOT_EMPTY:
							wrapper.isNotNull(columnName);
							break;
						case NE:
							wrapper.ne(columnName, value);
							break;
						case NOT_IN:
							wrapper.notIn(columnName, value);
							break;
						case EQ: default:
							wrapper.eq(columnName, value);
							break;
						}
					}else {
						wrapper.eq(columnName, value);
					}
				}
			}
		}
		return wrapper;
	}
}
