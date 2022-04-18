package com.mana.blogapi.utils;

import com.mana.blogapi.dao.pojo.SysUser;

public class UserThreadLocal {
    //线程变量隔离
    private UserThreadLocal() {
    }

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser) {
        LOCAL.set(sysUser);
    }

    public static SysUser get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }

}
