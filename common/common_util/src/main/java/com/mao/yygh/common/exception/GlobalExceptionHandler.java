package com.mao.yygh.common.exception;

import com.mao.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
* Description:全局异常处理类
* date: 2023/2/3 19:21
* @author: MaoJY
* @since JDK 1.8
*/
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(YyghException.class)
    public Result error(YyghException e) {
        return Result.build(e.getCode(), e.getMessage());
    }
}
