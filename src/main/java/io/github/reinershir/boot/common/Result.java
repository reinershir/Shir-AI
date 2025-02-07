package io.github.reinershir.boot.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.github.reinershir.boot.contract.ShirBootContracts;
import io.github.reinershir.boot.core.international.IMessager;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO")
@JsonInclude(value = JsonInclude.Include.ALWAYS)//Even if the returned object is null, it will still be returned when this annotation is added.
public class Result<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -472604879542896258L;

	@Schema(description = "Response Message",  nullable = false, example = "success！")
	private String message;
	@Schema(description = "The return result code, by default 00000 indicates a successful request.",  required = true, example = "00000")
	private String code;
	@Schema(description = "Return Value",  nullable = true)
	private T data = null;
	
	public Result(T value) {
		this.data=value;
		initSuccess();
	}
	
	public Result(String message,T value) {
		this.data=value;
		initSuccess();
		this.message=message;
	}
	
	public Result() {
		initSuccess();
	}
	
	private void initSuccess() {
		this.message = IMessager.getInstance().getMessage("message.success");
		this.code = ShirBootContracts.RESP_CODE_SUCCESS;
	}
	
	public static <T> Result<T> ok() {
		return new Result<T>();
	}
	
	public static <T> Result<T> ok(T value) {
		return new Result<T>(value);
	}
	public static <T> Result<T> ok(String message) {
		return new Result<>(message,ShirBootContracts.RESP_CODE_SUCCESS,true);
	}
	
	public static <T> Result<T> ok(String message,T value) {
		return new Result<T>(message,value);
	}
	
	public static <T> Result<T> failed() {
		return new Result<T>(IMessager.getInstance().getMessage("message.failed"),ShirBootContracts.RESP_CODE_FAILE,false);
	}
	
	public static <T> Result<T> failed(String message) {
		return new Result<T>(message,ShirBootContracts.RESP_CODE_FAILE,false);
	}
	
	public static <T> Result<T> failed(String message,String code) {
		return new Result<T>(message,code,false);
	}
	
	public Result(String message, String code,boolean isSuccess) {
		super();
		this.message = message;
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	

}
