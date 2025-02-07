package io.github.reinershir.boot.core.query.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryRule {

	/**
	* @Function: QueryRule.java
	* @Description: 指示在调用QueryHelper.initQueryWrapper()时如何构造查询条件,默认是eq
	* @param: @return
	* @return：QueryRuleEnum
	* @version: v1.0.0
	* @author: ReinerShir
	* @date: 2023年7月30日 下午11:02:22 
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2023年7月30日     ReinerShir       v1.0.0         
	 */
	QueryRuleEnum value() default QueryRuleEnum.EQ;
}
