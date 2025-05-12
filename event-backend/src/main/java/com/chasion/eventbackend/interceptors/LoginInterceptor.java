package com.chasion.eventbackend.interceptors;

import com.chasion.eventbackend.utils.JwtUtil;
import com.chasion.eventbackend.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行所有 OPTIONS 请求
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true; // 直接放行，不验证 Token
        }

        // 令牌验证
        String token = request.getHeader("Authorization");
        try {
            // 解析成功
            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);
            return true;
        }catch (Exception e){
            response.setStatus(401);
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
