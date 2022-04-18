package com.mana.blogapi.service;

import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.params.LoginParam;

public interface LoginService {


    /**
     * 登录功能
     *
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);


    /**
     * 退出功能
     *
     * @param token
     * @return
     */
    Result logout(String token);
}
