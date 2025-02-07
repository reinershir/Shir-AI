package io.github.reinershir.boot.common;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import io.github.reinershir.boot.config.dateformat.DateFormater;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseController {
	

	@InitBinder 
	public void initBinder(WebDataBinder binder){
		//解除spring mvc list参数限制长度问题
        binder.setAutoGrowCollectionLimit(1024);
		binder.registerCustomEditor(String.class,new StringTrimmerEditor(true));
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new DateFormater(log), true));
        binder.registerCustomEditor(Integer.class,  new PropertyEditorSupport(){
        	@Override
        	public void setAsText(String text) {
	        	if(StringUtils.hasText(text)){
	        		setValue(Integer.valueOf(text));
	        	}
        	}
        });
		binder.registerCustomEditor(long.class, new CustomNumberEditor(Long.class, true));
		binder.registerCustomEditor(double.class,new CustomNumberEditor(Double.class,true));
		binder.registerCustomEditor(float.class, new CustomNumberEditor(Float.class,true));
	} 
}
