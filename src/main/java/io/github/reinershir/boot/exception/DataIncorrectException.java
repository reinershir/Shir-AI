package io.github.reinershir.boot.exception;

public class DataIncorrectException extends BaseException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -932495155700141970L;
	public DataIncorrectException(String msg,Object ...formatParam){
		super(msg,formatParam);
	}
	public DataIncorrectException(String msg,String code,Object ...formatParam){
		super(msg,code,formatParam);
	}
}
