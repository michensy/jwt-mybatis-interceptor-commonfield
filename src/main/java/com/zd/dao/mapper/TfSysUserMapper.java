package com.zd.dao.mapper;

import com.zd.dao.model.TfSysUser;
import com.zd.mapper.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TfSysUserMapper extends MyMapper<TfSysUser> {
    void insertBatch(@Param(value = "users") List<TfSysUser> users);
}