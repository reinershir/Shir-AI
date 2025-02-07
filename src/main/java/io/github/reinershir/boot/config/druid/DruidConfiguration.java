package io.github.reinershir.boot.config.druid;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;


/**
 * 打开druid sql监控功能
 * @author ReinerShir
 *
 */
@Configuration
public class DruidConfiguration {

    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServle(){
        ServletRegistrationBean<StatViewServlet> servletRegistrationBean=new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
        //IP白名单
        //servletRegistrationBean.addInitParameter("allow","192.168.1.12,127.0.0.1");
        //IP黑名单
        //servletRegistrationBean.addInitParameter("deny","192.168.4.23");
        //控制台用户
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","admin123456");
        //是否能够重置数据
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }
    @Bean
    public FilterRegistrationBean<WebStatFilter> statFilter(){
        FilterRegistrationBean<WebStatFilter> filterRegistrationBean=new FilterRegistrationBean<>(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.html,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}