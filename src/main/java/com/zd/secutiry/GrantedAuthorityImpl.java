package com.zd.secutiry;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by zDong on 2018/11/15 下午5:36
 */
public class GrantedAuthorityImpl implements GrantedAuthority {
    private String authority;

    GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
