package com.sqx.config;

import com.alibaba.fastjson.JSON;
import com.sqx.common.utils.IPUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

//@Component
public class AntiShakeFilter extends OncePerRequestFilter {

    private final Cache<String, Boolean> requestCache;

    public AntiShakeFilter() {
        requestCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS) // 设置防抖时间为5秒
                .maximumSize(1000) // 设置缓存的最大容量  
                .build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 假设你从请求中提取出一个可以用来标识请求唯一性的key，比如用户ID和请求路径的组合  
        String key = extractUniqueKey(request);

        // 检查缓存中是否存在这个key  
        if (requestCache.asMap().containsKey(key)) {
            // 如果存在，说明是防抖时间窗口内的重复请求，可以选择记录日志、返回特定响应等  
            // 这里简单示例，只是记录日志并结束过滤链  
            // 注意：在实际应用中，你可能需要发送一个特定的响应给客户端，而不是继续处理请求  
            // 例如，可以设置一个HTTP 429状态码（Too Many Requests）  
            // ...
             // 终止处理，不继续向下传递请求
            // 发送一个HTTP 429状态码（Too Many Requests）
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            // 设置响应内容类型（可选）
            response.setContentType("application/json;charset=UTF-8");
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("code",429);
            resultMap.put("msg","请求过快~");
            // 写入响应体，例如JSON格式的错误消息
            try (PrintWriter out = response.getWriter()) {
                // 将Map转换为JSON字符串并写入响应体
                String jsonString = JSON.toJSONString(resultMap);
                out.println(jsonString);
//                out.println("{\"error\": \"Too Many Requests. Please try again later.\"}");
            } catch (IOException e) {
                // 处理写入异常
                e.printStackTrace();
            }

            // 由于已经发送了响应，所以不需要继续处理过滤器链
            return; // 终止处理，不继续向下传递请求
        }

        // 如果缓存中不存在这个key，将key加入缓存，并继续处理请求  
        requestCache.put(key, true); // 这里的值通常是无关紧要的，因为我们只关心key是否存在  

        // 继续处理请求（通过调用filterChain.doFilter()）  
        filterChain.doFilter(request, response);
    }

    // 辅助方法，用于从请求中提取唯一键  
    private String extractUniqueKey(HttpServletRequest request) {
        String ipAddr = IPUtils.getIpAddr(request);
        String requestURI = request.getRequestURI();
        // 根据你的应用逻辑提取唯一键，例如：userId + requestURI  
        // ...  
        return ipAddr+":"+requestURI;
    }
}