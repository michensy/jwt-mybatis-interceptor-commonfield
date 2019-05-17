package com.zd.util;


import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Zidong
 * @date 2019/4/30 5:41 PM
 */
public class WebUtils {
    private static final String[] IP_HEADERS = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};

    public WebUtils() {
    }

    public static Map<String, String> queryStringToMap(String queryString, String charset) {
        try {
            String[] decode = URLDecoder.decode(queryString, charset).split("&");
            Map<String, String> map = new HashMap(decode.length);
            for (String keyValue : decode) {
                String[] kv = keyValue.split("[=]", 2);
                map.put(kv[0], kv.length > 1 ? kv[1] : "");
            }
            return map;
        } catch (UnsupportedEncodingException var9) {
            throw new UnsupportedOperationException(var9);
        }
    }

    public static HttpServletRequest getHttpRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception var1) {
            return null;
        }
    }

    public static HttpServletResponse getHttpResponse() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        } catch (Exception var1) {
            return null;
        }
    }

    public static Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap();
        Enumeration enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()) {
            String name = String.valueOf(enumeration.nextElement());
            parameters.put(name, request.getParameter(name));
        }
        return parameters;
    }

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap();
        Enumeration enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static String getIpAddr(HttpServletRequest request) {
        for (String ipHeader : IP_HEADERS) {
            String ip = request.getHeader(ipHeader);
            if (!StringUtils.isEmpty(ip) && !ip.contains("unknown")) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    public static String getBasePath(HttpServletRequest request) {
        String path = request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        if (maxAge > 0) {
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        }
    }

    public static String getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            Cookie cookie = cookieMap.get(name);
            return cookie.getValue();
        } else {
            return null;
        }
    }

    protected static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Map<String, Cookie> cookieMap = new HashMap(cookies.length);
            for (Cookie cooky : cookies) {
                cookieMap.put(cooky.getName(), cooky);
            }
            return cookieMap;
        }
        return new HashMap();
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        String cookies = getCookieByName(request, name);
        if (!StringUtils.isEmpty(cookies)) {
            addCookie(response, name, null, 0);
        }
    }
}
