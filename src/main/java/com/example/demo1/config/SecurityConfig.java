package com.example.demo1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 开启CORS配置
                .cors(AbstractHttpConfigurer::disable)
                // 关闭CSRF防护（前后端分离场景）
                .csrf(AbstractHttpConfigurer::disable)
                // 设置无状态会话（JWT场景常用）
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 配置接口访问规则
                .authorizeHttpRequests(auth -> auth
                        // 放行POST /api/users（注册接口）
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        // 放行POST /api/users/login（登录接口）
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )
                // 关闭默认的表单登录
                .formLogin(AbstractHttpConfigurer::disable)
                // 关闭默认的HTTP Basic认证
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // 补充全局CORS配置，和上面的cors()配合使用
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}