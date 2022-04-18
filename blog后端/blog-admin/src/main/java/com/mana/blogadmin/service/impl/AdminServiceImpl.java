package com.mana.blogadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mana.blogadmin.mapper.AdminMapper;
import com.mana.blogadmin.pojo.Admin;
import com.mana.blogadmin.pojo.Permission;
import com.mana.blogadmin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findAdminByUsername(String username) {
        LambdaQueryWrapper<Admin> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Admin::getUsername, username);
        lambdaQueryWrapper.last("limit 1");
        Admin admin = adminMapper.selectOne(lambdaQueryWrapper);
        return admin;
    }

    @Override
    public List<Permission> findPermissionsByAdminId(Long adminId) {
        return adminMapper.findPermissionsByAdminId(adminId);
    }
}
