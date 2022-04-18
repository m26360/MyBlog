package com.mana.blogadmin.service;

import com.mana.blogadmin.model.param.PageParam;
import com.mana.blogadmin.pojo.Permission;
import com.mana.blogadmin.vo.Result;

public interface PermissionService {

    /**
     * 要的数据为  管理台表  的所有字段  Permission对象
     *
     * @param pageParam
     */
    Result listPermission(PageParam pageParam);

    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);
}
