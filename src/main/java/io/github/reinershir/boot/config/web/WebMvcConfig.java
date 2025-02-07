package io.github.reinershir.boot.config.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import io.github.reinershir.auth.interceptor.AuthenticationInterceptor;
import io.github.reinershir.boot.config.dateformat.DateFormater;

/**
 * @date: 2019年7月11日 下午2:22:19
 * @author ReinerShir
 * @Description:Spring mvc自定义配置类，继承WebMvcAutoConfiguration的作用是在其默认配置的基础上添加自己的配置
 */

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 设置允许跨域的路径
		registry.addMapping("/**")
				// 设置允许跨域请求的域名
				.allowedOriginPatterns("*")
				// 是否允许证书 不再默认开启
				.allowCredentials(true)
				// 设置允许的方法
				.allowedMethods("*")
				// 跨域允许时间
				.maxAge(3600);
	}
	
	@Value("${spring.profiles.active}") 
	String profiles;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**");
		//添加图片访问映射
	    registry.addResourceHandler("/images/**").addResourceLocations("file:"+
		(!profiles.equals("dev")?"/root/shir/":System.getProperty("user.dir"))+"/file/images/");
	}

	@Autowired(required = false)
	AuthenticationInterceptor authenticationInterceptor;

	/**
	 * 添加拦截器 
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (authenticationInterceptor != null) {
			registry.addInterceptor(authenticationInterceptor);
		}
		//添加国际化拦截器
		registry.addInterceptor(localeChangeInterceptor());
	}
	
   /**
   * 国际化拦截器
    */
   @Bean
   public LocaleChangeInterceptor localeChangeInterceptor() {
       LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
       localeChangeInterceptor.setParamName("lang");
       return localeChangeInterceptor;
   }

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = converter.getObjectMapper();
		SimpleModule simpleModule = new SimpleModule();
		// 生成JSON时,将所有Long转换成String
		// simpleModule.addSerializer(Long.class,
		// ToStringSerializer.instance);//当时为何要将Long转成string来着？
		// simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		objectMapper.registerModule(simpleModule);
		// 时间格式化
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setDateFormat(new DateFormater());
		// 设置格式化内容
		converter.setObjectMapper(objectMapper);
		converters.add(1, converter);
		// 需要追加byte，否则springdoc-openapi接口会响应Base64编码内容，导致接口文档显示失败
		// https://github.com/springdoc/springdoc-openapi/issues/2143
		// 解决方案
		converters.add(0, new ByteArrayHttpMessageConverter());
	}

}
