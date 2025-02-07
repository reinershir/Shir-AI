package io.github.reinershir.boot.exception;

public class BusinessException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7193838327170006378L;
	public BusinessException(String message,Object ...formatParam){
		super(message,formatParam);
	}
	public BusinessException(String message,String code,Object ...formatParam){
		super(message,code,formatParam);
	}
}
