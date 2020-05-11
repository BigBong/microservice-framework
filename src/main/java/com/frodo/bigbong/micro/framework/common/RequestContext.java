package com.frodo.bigbong.micro.framework.common;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RequestContext {

    public static final String SESSION_USER_ID = "session_user_id";
    public static final String SESSION_USER_NAME = "session_user_name";
    public static final String SESSION_ORG_ZONE_ID = "session_org_zone_id";

    public static final String SESSION_ORG_ID = "session_org_id";
    public static final String SESSION_ORG_TYPE = "session_org_type";

    /**
     * 默认系统用户id
     **/
    public static final Long SYS_DEFAULT_USER_ID = -1L;

    /**
     * 默认系统用户名
     **/
    public static final String SYS_DEFAULT_USER_NAME = "system";
    public static final String SESSION_REMOTE_ADDRESS = "session_remote_address";
    public static final String REQUEST_ID = "Request-Id";
    public static final String USER_AGENT = "user-agent";
    //当前线程没有request请求的情况下，需要在整个线程生命周期中使用的变量放在kvHolder中
    private static final ThreadLocal<Map<String, Object>> kvHolder = new ThreadLocal<>();

    private RequestContext() {
    }

    public static void remove() {
        kvHolder.remove();
    }

    /**
     * 获取当前登录人id
     */
    public static Long getCurrentUserId() {
        Object o = getSessionAttribute(SESSION_USER_ID);
        return o != null ? Long.valueOf(o.toString()) : SYS_DEFAULT_USER_ID;
    }

    public static String getCurrentUserName() {
        Object o = getSessionAttribute(SESSION_USER_NAME);
        return o != null ? o.toString() : SYS_DEFAULT_USER_NAME;
    }

    public static void setCurrentUserId(Long userId) {
        setSessionAttribute(SESSION_USER_ID, userId);
    }

    public static void setCurrentUserName(String userName) {
        setSessionAttribute(SESSION_USER_NAME, userName);
    }

    public static void setCurrentOrgZoneId(Long orgZoneId) {
        setSessionAttribute(SESSION_ORG_ZONE_ID, orgZoneId);
    }

    public static void setSessionAttribute(String key, Object value) {
        Map<String, Object> map = kvHolder.get();
        if (map == null) {
            map = new HashMap<String, Object>();
            kvHolder.set(map);
        }
        key = key.toLowerCase();
        map.put(key, value);
    }

    public static Object getSessionAttribute(String key) {
        Map<String, Object> map = kvHolder.get();
        key = key.toLowerCase();
        if (map == null || !map.containsKey(key)) {
            return null;
        }
        return map.get(key);
    }

    public static String getRemoteAddress() {
        Object o = getSessionAttribute(SESSION_REMOTE_ADDRESS);
        return o != null ? o.toString() : null;
    }

    public static void setRemoteAddress(String address) {
        setSessionAttribute(SESSION_REMOTE_ADDRESS, address);
    }

    public static String getRequestId() {
        Object o = getSessionAttribute(REQUEST_ID);
        return o != null ? o.toString() : null;
    }

    public static void setRequestId(String requestId) {
        setSessionAttribute(REQUEST_ID, requestId);
    }

    public static void setOrgId(Long orgId) {
        setSessionAttribute(SESSION_ORG_ID, orgId);
    }

    public static Long getOrgId() {
        Object o = getSessionAttribute(SESSION_ORG_ID);
        return o != null ? Long.valueOf(o.toString()) : null;
    }

    public static Integer getOrgType() {
        Object o = getSessionAttribute(SESSION_ORG_TYPE);
        return o != null ? Integer.valueOf(o.toString()) : null;
    }

    public static void setOrgType(Integer orgType) {
        setSessionAttribute(SESSION_ORG_TYPE, orgType);
    }

    public static String getUserAgent() {
        Object o = getSessionAttribute(USER_AGENT);
        return o != null ? o.toString() : null;
    }

    public static void setUserAgent(String userAgent) {
        setSessionAttribute(USER_AGENT, userAgent);
    }


}
