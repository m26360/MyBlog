package com.mana.blogapi.service;


import com.mana.blogapi.dao.pojo.SysUser;

public interface TokenService {


    /**
     * 解析token
     *
     * @param token
     * @return
     */
    SysUser checkToken(String token);
}
