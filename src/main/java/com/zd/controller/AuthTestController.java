package com.zd.controller;

import com.sf.pg.entity.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zDong on 2018/11/15 下午5:36
 */
@RestController
public class AuthTestController {

    /**
     * 无需任何权限，只要认证通过就可以访问
     * @return
     */
    @RequestMapping(value = "/test01")
    public R usersList() {
        return R.success("请求成功！");
    }

    /**
     * 需要有 AUTH_WRITE权限才可以访问
     * @return
     */
    @GetMapping(value = "/app")
    public R hello() {
        return R.success("请求成功！");
    }

    /**
     * 需要有 ADMIN权限才可以访问
     * @return
     */
    @RequestMapping(value = "/wx")
    public R world() {
        return R.success("请求成功！");
    }
}
