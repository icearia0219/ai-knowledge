package com.aizs.conf;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.context.SaHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {

                }))
                .addPathPatterns("/**") // 所有请求都拦截
                .excludePathPatterns( // 排除不需要验证的请求路径
                        "/api/login",
                        "/api/register","/api/exam/records","/api/exam/records/export","/api/exam/records/{workerid}","/api/exam/records/{workerid}/export"
                );
    }
}
