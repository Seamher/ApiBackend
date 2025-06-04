package com.api.apibackend.config;


import com.api.apibackend.LoggingInterceptor;
import com.api.apibackend.interceptors.LoginInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebConfig 类用于配置 Spring MVC 的拦截器和 CORS 设置
 *
 * @author Seamher
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginInterceptors loginInterceptors;

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    public WebConfig(LoginInterceptors loginInterceptors) {
        this.loginInterceptors = loginInterceptors;
    }

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加 loginInterceptors 拦截器，并排除注册和登录路径
        registry.addInterceptor(loginInterceptors).excludePathPatterns("/user", "/login");
        registry.addInterceptor(new LoggingInterceptor())
                .addPathPatterns("/**"); // 拦截所有请求路径
    }

    /**
     * 配置 CORS 设置
     *
     * @param registry CORS 注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("Configuring CORS mappings");
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*");
        logger.info("CORS mappings configured to allow all origins, methods, and headers");
    }
}
