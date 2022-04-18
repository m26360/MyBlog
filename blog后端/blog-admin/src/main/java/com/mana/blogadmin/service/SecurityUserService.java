package com.mana.blogadmin.service;


import com.mana.blogadmin.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class SecurityUserService implements UserDetailsService {

    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //登陆的时候，会把username传递到这里
        //通过username查询 admin表，如果admin存在，将密码告诉spring security
        //如果不存在，返回null（认证失败）
        Admin admin = adminService.findAdminByUsername(username);
        if (admin == null) {
            //认证失败
            return null;
        }
        //验证密码交给Security
        UserDetails userDetails = new User(username, admin.getPassword(), new ArrayList<>());
        return userDetails;
    }
}
