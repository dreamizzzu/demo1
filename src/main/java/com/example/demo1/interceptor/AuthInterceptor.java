package com.example.demo1.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String method = request.getMethod();
        String uri = request.getRequestURI();

        boolean isCreateUser = "POST".equalsIgnoreCase(method) && "/api/users".equals(uri);
        boolean isGetUser = "GET".equalsIgnoreCase(method) && uri.startsWith("/api/users/");
        boolean isLogin = "POST".equalsIgnoreCase(method) && "/api/users/login".equals(uri);
        if (isCreateUser || isGetUser || isLogin) {
            return true;
        }

        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=utf-8");
            String errorJson = "{\"code\": 401, \"msg\": \"登录凭证已缺失，请重新登录\"}";
            response.getWriter().write(errorJson);
            return false;
        }

        return true;
    }
}
