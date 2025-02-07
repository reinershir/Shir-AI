package io.github.reinershir.boot.core.international;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import io.github.reinershir.boot.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IMessager {

	private Properties properties;
	private static IMessager instance;
	
	public IMessager() throws IOException {
		String defaultPath = "i18n/message.properties";
		String path = "i18n/message_"+LocaleContextHolder.getLocale().getLanguage().toLowerCase()+"_"+LocaleContextHolder.getLocale().getCountry()+".properties";
		
		Resource resource = new ClassPathResource(path);
	    try {
			this.properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (FileNotFoundException e) {
			this.properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(defaultPath));
			log.warn("Unable to retrieve internationalization configuration path :{}, will attempt to use default configuration path.",path);
	    	if(properties==null) {
	    		throw new BaseException("Can not find internationalization properties");
	    	}
		}
	}
	
	public static synchronized IMessager getInstance() {
			if(IMessager.instance==null) {
				try {
					IMessager.instance = new IMessager();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		return IMessager.instance;
		
	}
	
	public static String getMessageByCode(String code) {
		return IMessager.getInstance().getMessage(code);
	}
	
	public String getMessage(String code) {
		return this.properties.getProperty(code);
	}
}
