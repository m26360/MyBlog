package com.mana.blogapi.service;


import com.mana.blogapi.vo.Result;
import com.mana.blogapi.vo.params.RegisterParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional//事务
public interface RegisterService {

    /**
     * 用户注册
     *
     * @param registerParam
     * @return
     */

    Result register(RegisterParam registerParam);
}
