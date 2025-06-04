package com.api.apibackend.interceptors;

import com.api.apibackend.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * LoginInterceptors 类用于处理登录拦截
 *
 * @author Seamher
 */
@Component
public class LoginInterceptors implements HandlerInterceptor {

    /**
     * 请求处理前的拦截逻辑
     *
     * @param request  HTTP 请求对象
     * @param response HTTP 响应对象
     * @param handler  处理器对象
     * @return 是否继续执行后续的处理逻辑
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (request.getMethod().equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, HEAD, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        // 从请求头中获取令牌
        String token = request.getHeader("Authorization");
        try {
            // 解析令牌
            JWTUtils.parseToken(token);
            return true; // 令牌有效，继续处理请求
        } catch (Exception e) {
            // 令牌无效，设置响应状态为401
            response.setStatus(401);
            return false; // 拦截请求
        }
    }
}
