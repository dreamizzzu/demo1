package com.example.demo1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 移除了 addInterceptors 方法，只保留类结构
    // 后续如果需要配置CORS、视图解析器等，仍可以在这里添加方法
}