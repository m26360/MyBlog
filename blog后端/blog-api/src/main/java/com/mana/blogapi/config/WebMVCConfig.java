package com.mana.blogapi.config;

import com.mana.blogapi.handler.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    /**
     * 跨域问题？？？？？？？？？？？？？？？？？？
     *
     * @param registry
     */
    @Override
    //跨域问题？？？？？
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截test接口，后续会根据实际更改
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/test")
                .addPathPatterns("/articles/publish")//拦截发布文章功能
                .addPathPatterns("/comments/create/change");//拦截评论功能
    }
}
