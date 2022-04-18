package com.mana.blogapi.service.impl;

import com.alibaba.fastjson.JSON;
import com.mana.blogapi.dao.mapper.LoginMapper;
import com.mana.blogapi.dao.pojo.SysUser;
import com.mana.blogapi.service.LoginService;
import com.mana.blogapi.utils.JWTUtils;
import com.mana.blogapi.vo.ErrorCode;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginMapper loginMapper;
    //redis
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

//    @Autowired
//    SysUserService sysUserService;

    //加密盐（RegisterServiceImpl中也有）
    private static final String slat = "mana!@#";


    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1.检查用户名是否合法
         * 2.根据 account和 password去 mn_sys_user表中查询是否存在
         * 3.如果不存在，则登陆失败
         * 4.如果存在，使用 jwt生成 token(令牌)返回给前端
         * 5.token放入 redis当中， redis  token：user信息，设置过期时间
         * （登陆认证的时候，先认证 token字符串是否合法，再去 redis认证是否存在）
         *
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        //检查前端传来的account和password是否为空
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        //对密码进行加密
        password = DigestUtils.md5Hex(password + slat);//slat：加密盐
        //用account和password去数据库表中查询
        //此处为loginMapper的findUser方法，而非SysUserService的findUser方法
        SysUser sysUser = loginMapper.findUser(account, password);
        if (sysUser == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode()
                    ,
                    ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        //根据id获取token
        String token = JWTUtils.createToken(sysUser.getId());
        //将token放入redis
        redisTemplate.opsForValue().set("TOKEN_" + token,
                JSON.toJSONString(sysUser),//将sysUser转换成字符串
                1, TimeUnit.DAYS);//设置过期时间(一天)
        //将token返回给前端
        return Result.success(token);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_" + token);
        return Result.success(null);
    }
}
