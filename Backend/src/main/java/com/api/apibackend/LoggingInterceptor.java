package com.api.apibackend;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    // 在请求开始时记录开始时间
    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime); // 将开始时间存入请求属性
        logger.info("Request [{}] started at: {}", request.getRequestURI(), startTime);
        return true; // 返回 true 表示继续处理请求
    }

    // 在请求完成后记录结束时间和持续时间
    @Override
    public void afterCompletion(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime"); // 获取开始时间
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime; // 计算持续时间

        logger.info("Request [{}] completed at: {}, duration: {} ms", request.getRequestURI(), endTime, duration);
    }
}