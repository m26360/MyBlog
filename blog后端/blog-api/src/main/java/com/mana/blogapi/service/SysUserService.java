package com.mana.blogapi.service;

import com.mana.blogapi.dao.pojo.SysUser;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.UserVo;


public interface SysUserService {

    UserVo findUserVoById(Long id);

    SysUser findUserById(Long authorId);

    /**
     * 见名知意
     * *********此方法没有用于 LoginController *************
     */
    SysUser findUser(String account, String password);

    /**
     * 根据 token查询用户信息
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查询用户信息
     *
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 将新用户保存到数据库
     *
     * @param sysUser
     */
    void save(SysUser sysUser);
}
