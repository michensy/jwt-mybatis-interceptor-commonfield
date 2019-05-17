package com.zd.util;


import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by zDong on 2018/5/25.
 */
public class Oauth2Util {

    public static UserJwt getUserJwtFromHeader() {
        HttpServletRequest request = WebUtils.getHttpRequest();
        if (request == null) {
            return null;
        }
        //取出头信息
        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization) || !authorization.contains("Bearer")) {
            return null;
        }
        //从Bearer 后边开始取出token
        String token = authorization.substring(7);
        Map<String, String> jwtClaims = null;
        try {
            //解析jwt
            Jwt decode = JwtHelper.decode(token);
            //得到 jwt中的用户信息
            String claims = decode.getClaims();
            //将jwt转为Map
            jwtClaims = JSON.parseObject(claims, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jwtClaims == null) {
            return null;
        }
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) jwtClaims.get("authorities"));
        UserJwt userJwt = new UserJwt();
        userJwt.setId(jwtClaims.get("uid"));
        userJwt.setUserName(jwtClaims.get("sub"));
        userJwt.setAuthorities(authorities);
        return userJwt;
    }

    @Data
    public static class UserJwt {

        private String id;

        private String userName;

        private List<GrantedAuthority> authorities;

    }

    public static String getUserName() {
        UserJwt userJwt = getUserJwtFromHeader();
        if (userJwt == null) {
            return "";
        }
        return userJwt.getUserName();
    }

    public static String getUserId() {
        UserJwt userJwt = getUserJwtFromHeader();
        if (userJwt == null) {
            return "";
        }
        return userJwt.getId();
    }
}
