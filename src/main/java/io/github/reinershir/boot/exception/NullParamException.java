package io.github.reinershir.boot.exception;

public class NullParamException extends NullPointerException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -932495155700141970L;
	private String msg;
	public NullParamException(String msg){
		super(msg);
		this.msg=msg;
	}
	
	public String getMessage(){
		return msg;
	}
}
