package com.mana.blogadmin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mana.blogadmin.pojo.Admin;
import com.mana.blogadmin.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
    @Select("select * from mn_permission where id in (select permission_id from mn_admin_permission where admin_id=#{adminId} )")
    List<Permission> findPermissionsByAdminId(Long adminId);
}
