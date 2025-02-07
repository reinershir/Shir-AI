package io.github.reinershir.boot.config.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
public class SwaggerConfig  implements WebMvcConfigurer{
    /**
     * 根据@Tag 上的排序，写入x-order
     *
     * @return the global open api customizer
     */
    @Bean
    public GlobalOpenApiCustomizer orderGlobalOpenApiCustomizer() {
        return openApi -> {
            if (openApi.getTags()!=null){
                openApi.getTags().forEach(tag -> {
                    Map<String,Object> map=new HashMap<>();
                    map.put("x-order",RandomUtil.randomInt(0,100));
                    tag.setExtensions(map);
                });
            }
//            if(openApi.getPaths()!=null){
//                openApi.addExtension("x-test123","333");
//                openApi.getPaths().addExtension("x-abb",RandomUtil.randomInt(1,100));
//            }

        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shir Boot API")
                        .version("1.0")
                        .description( "springdoc-openapi support")
                        .termsOfService("http://github.com/reinershir")
                        .license(new License().name("Apache 2.0")
                                .url("http://github.com/reinershir")));
    }
    
    @Bean
    public GroupedOpenApi sysApi() {
        return GroupedOpenApi.builder()
                .group("系统接口")
                .pathsToMatch("/**")
                // 添加自定义配置，这里添加了一个用户认证的 header，否则 knife4j 里会没有 header
                .addOperationCustomizer((operation, handlerMethod) -> operation.security(
                        Collections.singletonList(new SecurityRequirement().addList("Access-Token")))
                )
                .build();
    }

    /**
    *
    * 显示swagger-ui.html文档展示页，还必须注入swagger资源：
    *
    * @param registry
    */
   @Override
   public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
       registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
       registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
   }
}