package cn.itxsl.kernel.exception;

import cn.itxsl.kernel.model.dto.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author ：itxsl
 * @date ：Created in 2019/1/13 16:02
 * @description：全局异常处理
 */
@RestControllerAdvice
public class GlobalException {

    /**
     * 验证信息异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public Result bindExceptionHandler(BindException e) {
        List<FieldError> errors;
        errors = e.getBindingResult().getFieldErrors();
        StringBuffer sb = new StringBuffer();
        for (FieldError error : errors) {
            sb.append(error.getField() + "：" + error.getDefaultMessage() + "；");
        }
        String message = sb.substring(0, sb.lastIndexOf("；"));
        return new Result(400, message);
    }
}
