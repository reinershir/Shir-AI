package io.github.reinershir.boot.exception;

public class AnnotationException extends Exception{

	private static final long serialVersionUID = -932495155730041970L;
	private String msg;
	public AnnotationException(String msg){
		super(msg);
		this.msg=msg;
	} 
	
	public String getMessage(){
		return msg;
	}
}
