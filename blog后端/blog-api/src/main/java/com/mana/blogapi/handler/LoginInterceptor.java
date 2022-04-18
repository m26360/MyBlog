package com.mana.blogapi.handler;

import com.alibaba.fastjson.JSON;
import com.mana.blogapi.dao.pojo.SysUser;
import com.mana.blogapi.service.TokenService;
import com.mana.blogapi.utils.UserThreadLocal;
import com.mana.blogapi.vo.ErrorCode;
import com.mana.blogapi.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 配置登录拦截器
 */

@Component
@Slf4j//日志
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    TokenService tokenService;

    @Override
    //在执行controller方法（Handler）之前执行
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 1.首先判断请求的接口路径是否为HandlerMethod（controller方法）
         * 2.判断token是否为空，如果为空，未登录
         * 3.如果token不为空，进行token验证 TokenService中的checkToken方法
         * 4.如果认证成功，则放行
         */
        if (!(handler instanceof HandlerMethod)) {
            //handler可能是 RequestResourceHandler（请求资源）
            //程序访问静态资源默认去 classpath下的 static目录访问
            return true;
        }
        String token = request.getHeader("Authorization");
        //打印log日志
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}", requestURI);
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");
        //判断token是否为空
        if (StringUtils.isBlank(token)) {
            //返回未登录信息
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            //告诉浏览器返回类型json，字符集utf-8
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //进行token验证
        SysUser sysUser = tokenService.checkToken(token);
        if (sysUser == null) {
            //返回未登录信息
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
            //告诉浏览器返回类型json，字符集utf-8
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        //登录验证成功，放行
        //并希望在controller中，直接获取用户的信息
        //将sysUser放入UserThreadLocal，用完需删除
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除ThreadLocal中用完的信息，会有内存泄漏的风险
        UserThreadLocal.remove();
    }
}
