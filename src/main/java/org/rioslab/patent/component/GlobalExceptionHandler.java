package org.rioslab.patent.component;


import lombok.extern.slf4j.Slf4j;
import org.rioslab.patent.api.CommonResult;
import org.rioslab.patent.api.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * TODO 之后要针对每个异常进行单独处理
 *
 * @author thomas
 * @version 1.0
 * @date 2020/12/19 21:36 下午
 * 统一异常处理
 */
@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * NoHandlerFoundException 404 异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult<?> handleNotFoundException(NoHandlerFoundException e) throws Throwable {
        // 404不需要处理 直接返回就行
        return CommonResult.fail(ResultCode.NotFound);
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult<?> handleNullPointerException(NullPointerException e) throws Throwable {
        log.error(e.getMessage());
        return CommonResult.fail(ResultCode.InternalServerError);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) throws Throwable {
        log.error(e.getMessage());
        return CommonResult.fail(ResultCode.InternalServerError);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult<?> handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder();
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message.append(fieldError.getField()).append(fieldError.getDefaultMessage());
            }
        }
        return CommonResult.fail(message.toString());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult<?> handleValidException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder();
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message.append(fieldError.getField()).append(fieldError.getDefaultMessage());
            }
        }
        return CommonResult.fail(message.toString());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public CommonResult<?> handleException(Exception e) throws Throwable {
        log.error(e.getMessage());
        return CommonResult.fail(ResultCode.InternalServerError);
    }

}
