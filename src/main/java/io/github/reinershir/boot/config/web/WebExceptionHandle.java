package io.github.reinershir.boot.config.web;

import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import io.github.reinershir.boot.common.Result;
import io.github.reinershir.boot.contract.ShirBootContracts;
import io.github.reinershir.boot.exception.AuthenticationException;
import io.github.reinershir.boot.exception.BusinessException;
import io.github.reinershir.boot.exception.DataUpdateException;
import io.github.reinershir.boot.exception.NullParamException;
import io.github.reinershir.boot.exception.QueryException;

/**
 * spring mvc通用 http异常控制处理
 * @author ReinerShir
 *
 */
@RestControllerAdvice
public class WebExceptionHandle {
	
    private static Logger logger = LoggerFactory.getLogger(WebExceptionHandle.class);
    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败", e);
        return Result.failed("参数解析失败，请检请求参数格式！");
    }
    
    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法", e);
        return Result.failed("不支持的请求类型！");
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result<Object> handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error("不支持当前媒体类!", e);
        return Result.failed("不支持当前媒体类！");
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        logger.error("服务运行异常,异常信息：{}", e.getMessage(),e);

        return Result.failed("服务器异常！",ShirBootContracts.RESP_CODE_FAILE);
    }
    
    
    /**
     * @Description: 参数不合法异常，捕捉spring validate 验证抛出的异常 
     * @param: @param e
     * @param: @return      
     * @return: Result<Object>      
     * @throws
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn("参数错误，请检查参数！提示信息：{}", e.getMessage());
        return Result.failed("参数错误，"+e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),ShirBootContracts.RESP_CODE_FAILE_PARAM);
    }
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException e) {
        logger.warn("参数校检失败，请检查参数！ 提示信息：{}",e.getMessage());
        return Result.failed("参数校检失败，"+e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),ShirBootContracts.RESP_CODE_FAILE_PARAM);
    }
    
    
    
    /**
     * 数据查询异常
     * @param ex
     * @param request
     * @return
     * @throws IOException
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({QueryException.class})
    public Result<Object> handleQueryException(QueryException ex, WebRequest request) {
    	logger.error("查询数据时出错！出错信息：{}",ex.getMessage(),ex);
    	return Result.failed("查询数据时出错！",ShirBootContracts.RESP_CODE_QUERY_ERROR);
    }
    
    /**
     * 数据修改异常
     * @param ex
     * @param request
     * @return
     * @throws IOException
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({DataUpdateException.class})
    public Result<Object> handleDataUpdateException(DataUpdateException ex, WebRequest request) {
    	logger.error("修改数据时出错！出错信息：{}",ex.getMessage(),ex);
    	return Result.failed("修改数据时出错！",ShirBootContracts.RESP_CODE_DATA_UPDATE_ERROR);
    }
    
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({NullParamException.class})
    public Result<Object> handleNullParamException(RuntimeException ex, WebRequest request){
    	return Result.failed("参数不能为空!",ShirBootContracts.RESP_CODE_FAILE_PARAM);
    }
    /**
     * @Description: 捕捉上传文件大小超过限制异常，返回HTTP STATUS 200，并返回自定义状态码
     * @param: @param e
     * @param: @return      
     * @return: Result<Object>      
     * @throws
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public Result<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e){
    	return Result.failed("超出文件上传大小限制！",ShirBootContracts.RESP_CODE_UPLOAD_MAX_FAILE);
    }
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({FileSizeLimitExceededException.class})
    public Result<Object> handleFileSizeLimitExceededException(FileSizeLimitExceededException e){
    	return Result.failed("超出文件上传大小限制！",ShirBootContracts.RESP_CODE_UPLOAD_MAX_FAILE);
    }
    
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AuthenticationException.class})
    public Result<Object> handleAuthenticationException(AuthenticationException e){
    	return Result.failed("无访问权限！参考信息："+e.getMessage(),ShirBootContracts.RESP_CODE_UPLOAD_MAX_FAILE);
    }
    
    /**
     * @Description: 业务异常控制，如是业务异常则转换为正常状态码200并返回业务异常信息
     * @param: @param e
     * @param: @return      
     * @return: Result<Object>      
     * @throws
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler({BusinessException.class})
    public Result<Object> handleBusinessException(BusinessException e){
    	logger.error("出现业务异常！异常信息:{}",e.getMessage());
    	return Result.failed(e.getMessage(),e.getCode());
    }
    
    
}