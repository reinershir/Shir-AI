package io.github.reinershir.boot.core.query.annotation;

import org.springframework.util.StringUtils;

public enum QueryRuleEnum {

	/**查询规则 大于*/
    GT(">","gt","大于"),
    /**查询规则 大于等于*/
    GE(">=","ge","大于等于"),
    /**查询规则 小于*/
    LT("<","lt","小于"),
    /**查询规则 小于等于*/
    LE("<=","le","小于等于"),
    /**查询规则 等于*/
    EQ("=","eq","等于"),
    /**查询规则 不等于*/
    NE("!=","ne","不等于"),
    /**查询规则 包含*/
    IN("IN","in","包含"),
    /**查询规则 全模糊*/
    LIKE("LIKE","like","全模糊"),
    /**查询规则 左模糊*/
    LEFT_LIKE("LEFT_LIKE","left_like","左模糊"),
    /**查询规则 右模糊*/
    RIGHT_LIKE("RIGHT_LIKE","right_like","右模糊"),
    /** 值为空 */
    EMPTY("EMPTY","empty","值为空"),
    /** 值不为空 */
    NOT_EMPTY("NOT_EMPTY","not_empty","值不为空"),
    /**查询规则 不包含*/
    NOT_IN("NOT_IN","not_in","不包含"),
    HIDDEN("HIDDEN","hidden","隐藏");
    
	private String value;
    
    private String condition; 

    private String msg;

    QueryRuleEnum(String value, String condition, String msg){
        this.value = value;
        this.condition = condition;
        this.msg = msg;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public static QueryRuleEnum getByValue(String value){
    	if(!StringUtils.hasText(value)) {
    		return null;
    	}
        for(QueryRuleEnum val :values()){
            if (val.getValue().equals(value) || val.getCondition().equals(value)){
                return val;
            }
        }
        return  null;
    }
}
