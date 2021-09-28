package com.lp.demo.common.exception;

import com.lp.demo.common.result.JsonResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = DisplayableException.class)
    public JsonResult handle(DisplayableException e) {
        String[] result = e.getMessage().split("\\|");
        return new JsonResult<>(Integer.parseInt(result[0]), result[1]);
    }

    /**
     * 参数校验错误异常处理
     * 参数在进行绑定注解校验规则时出现异常处理
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public JsonResult handleValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = fieldError.getField()+fieldError.getDefaultMessage();
            }
        }
        return new JsonResult<>(404, message);
    }

    /**
     * 其他异常处理
     */
}
