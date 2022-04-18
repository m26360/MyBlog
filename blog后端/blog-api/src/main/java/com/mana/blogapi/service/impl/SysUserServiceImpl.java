package com.mana.blogapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mana.blogapi.dao.mapper.SysUserMapper;
import com.mana.blogapi.dao.pojo.SysUser;
import com.mana.blogapi.service.SysUserService;
import com.mana.blogapi.service.TokenService;
import com.mana.blogapi.vo.ErrorCode;
import com.mana.blogapi.vo.LoginUserVo;
import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TokenService tokenService;

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("游客");
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser, userVo);
        return userVo;
    }

    /**
     * 此处id为作者id，不是文章id
     */
    @Override
    public SysUser findUserById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("游客");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getAccount, account);
        lambdaQueryWrapper.eq(SysUser::getPassword, password);
        lambdaQueryWrapper.select(SysUser::getAccount, SysUser::getId, SysUser::getAvatar, SysUser::getNickname);
        lambdaQueryWrapper.last("limit 1");
        return sysUserMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.token合法性校验
         *   1)token是否为空
         *   2)解析是否成功
         * 2.redis是否存在
         * 3.如果校验失败，返回 null(error)
         * 4.如果成功，返回 LoginUserVo对象
         */
        //token合法性校验
        SysUser sysUser = tokenService.checkToken(token);
        if (sysUser == null) {//校验失败
            //token不合法
            return Result.fail(ErrorCode.TOKEN_ILLEGAL.getCode(), ErrorCode.TOKEN_ILLEGAL.getMsg());
        }
        //成功
        LoginUserVo loginUserVo = new LoginUserVo(sysUser.getId(),
                sysUser.getAccount(),
                sysUser.getNickname(),
                sysUser.getAvatar());
        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getAccount, account);
        lambdaQueryWrapper.last("limit 1");
        return sysUserMapper.selectOne(lambdaQueryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        /**
         * 保存用户 ，id会自动生成（mybatis-plus）
         * 默认生成id的方式是雪花算法,而非自增
         *
         */
        sysUserMapper.insert(sysUser);
    }
}
