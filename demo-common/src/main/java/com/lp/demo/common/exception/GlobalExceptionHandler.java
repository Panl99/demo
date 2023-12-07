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


    /**
     * 全局异常处理器执行优先级
     *
     * 虽然自定义异常DisplayableException处理方法放在了Exception处理方法的前面，但是抛自定义异常时却进了Exception的处理方法？
     * 那么：在同一个ExceptionHandler类，定义多个异常处理器，它们的加载顺序是怎样的？
     *
     * 结论：由ExceptionHandlerMethodResolver通过递归查找调用链，内部自己判断决定的，用户没办法直接干预，调用链越小，优先级越高。
     *
     * 怎么解决调用顺序问题？
     * 多个自定义ExceptionHandler类的执行顺序就是被Spring加载到容器中Bean的加载顺序，用户是可控的。
     * 所以把下边方法抽出来，改造成两个类，类上添加@Order来标识先后顺序，@Order(Ordered.HIGHEST_PRECEDENCE)越小优先级越高。
     *
     * 上述解决办法没用：DisplayableException抛出去是RunnableException异常？
     * 原因：使用了dubbo，dubbo的provider端抛异常时会被provider端的org.apache.dubbo.rpc.filter.ExceptionFilter拦截，里边重写了onResponse，将自定义异常转成了RunnableException。
     * 链接：https://zhuanlan.zhihu.com/p/111947357
     *
     */
    @ExceptionHandler
    @ResponseBody
    public JsonResult displayableExceptionHandler(DisplayableException de) {
        // TODO
        return new JsonResult<>();
    }

    @ExceptionHandler
    @ResponseBody
    public JsonResult exceptionHandler(Exception e) {
        // TODO
        return new JsonResult<>();
    }
}
