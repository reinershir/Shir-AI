package io.github.reinershir.boot.config.dateformat;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ParseException;
import org.springframework.util.StringUtils;

/**
 * @date:   2019年7月8日 下午4:51:43   
 * @author ReinerShir 
 * @Description: 日期转换类，目前用于spring mvc接收json日期的转换
 */
public class DateFormater extends DateFormat {

	Logger logger;
	
	public DateFormater(Logger logger) {
		this.logger=logger;
	}
	
	public DateFormater() {
		this.logger=LoggerFactory.getLogger(getClass());
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4876634368991167714L;


	private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		return dateTimeFormat.format(date, toAppendTo, fieldPosition);
	}

	@Override
	public Date parse(String source, ParsePosition pos) {
		Date date = null;
		try {
			if(StringUtils.hasText(source)) {
				if(source.indexOf(":")!=-1) {
					date = dateTimeFormat.parse(source,pos);
				}else {
					date = dateFormat.parse(source, pos);
				}
			}
		} catch (Exception e) {
			logger.error("日期转换异常，异常参考信息：{},{}",e.getMessage(),source);
		}

		return date;
	}

	@Override
	public Date parse(String source) throws ParseException, java.text.ParseException {
		Date date = null;
		try {
			if(StringUtils.hasText(source)) {
				if(source.indexOf(":")!=-1) {
					date = dateTimeFormat.parse(source);
				}else {
					date = dateFormat.parse(source);
				}
			}
			
		} catch (Exception e) {
			logger.error("日期转换异常，异常参考信息：{},{}",e.getMessage(),source);
			date = dateFormat.parse(source);
		}
		return date;
	}

	@Override
	public Object clone() {
	     return new DateFormater(logger);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
	
}
