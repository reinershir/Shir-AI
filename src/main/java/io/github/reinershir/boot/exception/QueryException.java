package io.github.reinershir.boot.exception;

public class QueryException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8486827963722637008L;
	public QueryException(String msg,Object ...formatParam){
		super(msg,formatParam);
	}
	
	public QueryException(String msg,String code,Object ...formatParam){
		super(msg,code,formatParam);
	}
}
