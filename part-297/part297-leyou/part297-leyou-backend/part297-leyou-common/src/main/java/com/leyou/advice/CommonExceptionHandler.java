package com.leyou.advice;

import com.leyou.myexception.ExceptionEnum;
import com.leyou.myexception.LeyouException;
import com.leyou.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


// 默认情况下 拦截所有的controller
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(LeyouException.class)
    public ResponseEntity<ExceptionResult> handlerException(LeyouException e){
        ExceptionEnum exceptionEnum = e.getExceptionEnum();
        return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
    }

}
