package com.mana.blogapi.controller;

import com.mana.blogapi.service.RegisterService;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.params.LoginParam;
import com.mana.blogapi.vo.params.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    RegisterService registerService;


    @PostMapping
    public Result register(@RequestBody RegisterParam registerParam) {
        return registerService.register(registerParam);
    }


}
