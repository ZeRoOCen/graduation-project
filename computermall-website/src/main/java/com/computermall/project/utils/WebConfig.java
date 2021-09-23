package com.computermall.project.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry)
    {
        //  "/**"：所有路径都可以被过滤，即不被拦截
        registry.addMapping("/**")
                    //只允许在此处定义的域名进行跨域请求，可以设置多个("xxx","xxx","xxx"...)；前端默认端口号使用8080
                .allowedOrigins("Http://localhost:8080","Http://localhost:8081")
                    //过滤所有请求头信息，还未使用到
                 .allowedHeaders("*")
                    //是否允许携带信息（true表示允许，false表示不允许）
                .allowCredentials(true)
                    //允许请求的类型
                .allowedMethods("GET","POST","DELETE","PUT","OPTIONS")
                    //最大响应时间
                .maxAge(3600*24);
    }
}
