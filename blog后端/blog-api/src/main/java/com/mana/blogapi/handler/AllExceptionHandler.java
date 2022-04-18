package com.mana.blogapi.handler;

import com.mana.blogapi.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 */

//对加了@Controller注解的方法进行拦截处理  （AOP的实现）
@ControllerAdvice
public class AllExceptionHandler {

    //进行异常处理，处理Exception.class的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public Result doException(Exception ex) {
        //暂时只打印，后期会进行记录处理
        ex.printStackTrace();
        return Result.fail(-999, "系统异常");
    }
}
