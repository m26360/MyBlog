package com.mana.blogapi.controller;

import com.mana.blogapi.service.LoginService;
import com.mana.blogapi.vo.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    /**
     * 通过使 redis失效实现退出登陆
     *
     * @param token
     * @return
     */
    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token) {
        return loginService.logout(token);
    }
}