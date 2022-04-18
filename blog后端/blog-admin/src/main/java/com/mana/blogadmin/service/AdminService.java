package com.mana.blogadmin.service;

import com.mana.blogadmin.pojo.Admin;
import com.mana.blogadmin.pojo.Permission;

import java.util.List;

public interface AdminService {

    Admin findAdminByUsername(String username);


    List<Permission> findPermissionsByAdminId(Long id);
}
