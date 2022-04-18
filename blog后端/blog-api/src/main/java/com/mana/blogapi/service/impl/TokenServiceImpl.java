package com.mana.blogapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.mana.blogapi.dao.pojo.SysUser;
import com.mana.blogapi.service.TokenService;
import com.mana.blogapi.utils.JWTUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public SysUser checkToken(String token) {
        //token是否为空
        if (StringUtils.isBlank(token)) {
            return null;
        }
        //解析token
        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map == null) {
            return null;
        }
        //redis中是否有，没有则是过期了
        String useJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(useJson)) {
            return null;
        }
        SysUser sysUser = JSON.parseObject(useJson, SysUser.class);
        return sysUser;
    }
}
