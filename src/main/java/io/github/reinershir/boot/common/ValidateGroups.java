package io.github.reinershir.boot.common;

import jakarta.validation.groups.Default;

/**
 * spring validate 分组类
 * @author ReinerShir 
 * @Description:
 */
public class ValidateGroups {

	//分组类要继承Default,spring validate才会验证其他未添加分组的验证
	
	/**
	 * 添加数据时的分组
	 * @author ReinerShir 
	 * @Description:
	 */
	public interface AddGroup extends Default{}
	/**
	 * 修改数据时的分组
	 * @author ReinerShir 
	 * @Description:
	 */
	public interface UpdateGroup extends Default{}
	/**
	 * 删除数据时的分组
	 * @author ReinerShir 
	 * @Description:
	 */
	public interface DeleteGroup extends Default{}
	
	/**
	 * 查询数据时的分组
	 * @author ReinerShir 
	 * @Description:
	 */
	public interface QueryGroup extends Default{}
	/**
	 * @author ReinerShir 
	 * @Description: 自定义的业务分组，按需求自由使用
	 */
	public interface BusinessGroup extends Default{}
}
