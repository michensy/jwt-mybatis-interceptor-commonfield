package com.zd.controller;

import com.zd.dao.model.TfSysUser;
import com.zd.service.UserService;
import com.sf.idworker.generator.IdWorkerInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zidong
 * @date 2019/5/17 11:06 AM
 */
@RestController
public class CommonFieldTestController {

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String addUser() {
        Long uid = IdWorkerInstance.getId();
        TfSysUser userDo = new TfSysUser();
        userDo.setUserId(uid);
        userDo.setValidTag(true);
        int insert = userService.addUser(userDo);
        if (insert == 0) {
            return "新增失败！";
        }
        return "新增成功，UID为：" + uid;
    }

    @GetMapping("/get")
    public TfSysUser getUser(String uid) {
        return userService.getUser(uid);
    }

    @GetMapping("update")
    public String updateUser(@RequestParam String uid) {
        TfSysUser tfSysUser = new TfSysUser();
        tfSysUser.setUserId(Long.valueOf(uid));
        int update = userService.updateUser(tfSysUser);
        if (update == 0) {
            return "更新失败！";
        }
        return "更新成功，UID为：" + uid;
    }
}
