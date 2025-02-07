package io.github.reinershir.boot.exception;

public class DataUpdateException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8486826963722637008L;
	public DataUpdateException(String msg,Object ...formatParam){
		super(msg,formatParam);
	}
	
	public DataUpdateException(String msg,String code,Object ...formatParam){
		super(msg,code,formatParam);
	}
}
