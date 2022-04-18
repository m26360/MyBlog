package com.mana.blogapi.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mana.blogapi.dao.pojo.SysUser;
import com.mana.blogapi.vo.params.LoginParam;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface LoginMapper extends BaseMapper<SysUser> {


    SysUser findUser(String account, String password);
}
