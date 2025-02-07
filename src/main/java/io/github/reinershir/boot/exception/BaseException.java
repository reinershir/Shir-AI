package io.github.reinershir.boot.exception;

import io.github.reinershir.boot.contract.ShirBootContracts;

public class BaseException extends RuntimeException{
	private static final long serialVersionUID = 3900739381315865122L;
	private String message;
	private String code;
	
	public BaseException(String message,Object ...formatParam) {
		super(String.format(message,formatParam));
		this.message = String.format(message,formatParam);
		this.code = ShirBootContracts.RESP_CODE_FAILE;
	}
	
	public BaseException(String message, String code,Object ...formatParam) {
		super();
		this.message = String.format(message,formatParam);
		this.code = code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public String getCode() {
		return code;
	}
	
}
