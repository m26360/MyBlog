package com.mana.blogapi.controller;

import com.mana.blogapi.service.LoginService;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {
    //不这样写，SysUserService只用它来完成操作 mn_sys_user表 就好
    //@Autowired
    //private SysUserService sysUserService;

    @Autowired
    LoginService loginService;


    /**
     * 登陆验证
     *
     * @param loginParam
     * @return
     */
    @PostMapping
    public Result login(@RequestBody LoginParam loginParam) {
        return loginService.login(loginParam);
    }


}
