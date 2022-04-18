package com.mana.blogapi.service.impl;


import com.alibaba.fastjson.JSON;
import com.mana.blogapi.dao.pojo.SysUser;
import com.mana.blogapi.service.RegisterService;
import com.mana.blogapi.service.SysUserService;
import com.mana.blogapi.utils.JWTUtils;
import com.mana.blogapi.vo.ErrorCode;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.params.RegisterParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class RegisterServiceImpl implements RegisterService {


    @Autowired
    SysUserService sysUserService;
    //redis
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //加密盐（LoginServiceImpl中也有）
    private static final String slat = "mana!@#";

    @Override
    public Result register(RegisterParam registerParam) {
        /**
         * 1.判断参数是否合法
         * 2.判断账户是否存在，存在则返回账号已被注册
         * 3.如果不存在，注册用户（保存到数据库中）
         * 4.生成 token
         * 5.将 token存入 redis并返回
         * 6.注意：加上事务，如果中间过程出现问题，注册的用户需要回滚  @Transactional（加在了RegisterService接口上）
         */
        //判断参数是否合法
        String account = registerParam.getAccount();
        String password = registerParam.getPassword();
        String nickname = registerParam.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //判断账户是否存在
        SysUser sysUser = sysUserService.findUserByAccount(account);
        //存在返回错误信息
        if (sysUser != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        //不存在，注册用户
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password + slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        //将新用户保存到数据库
        sysUserService.save(sysUser);
        //根据id获取token
        String token = JWTUtils.createToken(sysUser.getId());
        //将token放入redis
        redisTemplate.opsForValue().set("TOKEN_" + token,
                JSON.toJSONString(sysUser),//将sysUser转换成字符串
                1, TimeUnit.DAYS);//设置过期时间(一天)
        //将token返回给前端
        return Result.success(token);
    }
}
