package com.frodo.bigbong.micro.framework.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * @author: frodoking
 * @date: 2020/04/03
 * @description: 微服务后端的上下文拦截器
 */
@Slf4j
public class MicroServiceSessionInjector {

    public static void inject(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("Request Uri >> {}", request.getRequestURI());
        HttpSession session = request.getSession(true);
        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String key = attributeNames.nextElement();
            RequestContext.setSessionAttribute(key, session.getAttribute(key));
            if (log.isDebugEnabled()) {
                log.debug("Attributes {} {}", key, session.getAttribute(key));
            }
        }

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if (log.isDebugEnabled()) {
                log.debug("Headers {} {}", key, value);
            }
            if (StringUtils.isBlank(value) || value.equalsIgnoreCase("null")) {
                continue;
            }
            RequestContext.setSessionAttribute(key, value);
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                RequestContext.setSessionAttribute(cookie.getName(), cookie.getValue());
                if (log.isDebugEnabled()) {
                    log.debug("Cookies {} {}", cookie.getName(), cookie.getValue());
                }
            }
        }
        RequestContext.setRemoteAddress(servletRequest.getRemoteAddr());

        try {

            if (RequestContext.getCurrentUserId() != null) {
                MDC.put("userId", RequestContext.getCurrentUserId() + "");
            }

            if (StringUtils.isNotBlank(RequestContext.getCurrentUserName())) {
                RequestContext.setCurrentUserName(URLDecoder.decode(RequestContext.getCurrentUserName(), StandardCharsets.UTF_8.name()));
            }

            if (RequestContext.getRequestId() != null) {
                MDC.put("reqId", RequestContext.getRequestId() + "");
            }

        } catch (UnsupportedEncodingException e) {
            log.error("inject session error >> ", e);
        }

        filterChain.doFilter(servletRequest, servletResponse);

        MDC.clear();
        RequestContext.remove();
    }
}
