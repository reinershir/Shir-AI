package io.github.reinershir.boot.exception;

public class ParamException extends BaseException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -932495155700141970L;
	public ParamException(String formatMsg,Object ...formatParam){
		super(formatMsg,formatParam);
	}
}
