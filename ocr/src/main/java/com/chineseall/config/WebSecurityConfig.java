package com.chineseall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:08.
 */
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {
    /**
     * 登录session key
     */
    public static final String SESSION_KEY = "user";

    @Bean
    public WebConfig getSecurityInterceptor() {
        return new WebConfig();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }
}
