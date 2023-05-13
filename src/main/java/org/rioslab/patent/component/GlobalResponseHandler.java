package org.rioslab.patent.component;

import cn.hutool.json.JSONUtil;
import org.rioslab.patent.api.CommonResult;
import org.rioslab.patent.api.ResultCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author thomas
 * @version 1.0
 * @date 2020/12/19 21:36 下午
 * 统一返回值
 */
@Component
@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Class<?> declaringClass = methodParameter.getDeclaringClass();
        // 过滤swagger相关包
        if (declaringClass.getName().contains("springfox.documentation") || declaringClass.getName().contains("org.springframeworke")) {
            return false;
        }

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 返回错误码则直接设置状态码
        if (o instanceof ResultCode) {
            return CommonResult.fail((ResultCode) o);
        }
        // 如果是通用统一处理结果name直接返回
        if (o instanceof CommonResult) {
            return o;
        }
        // 如果是String类型需要另外处理，否则会报异常
        if (o instanceof String) {
            return JSONUtil.parseObj(
                CommonResult
                    .success()
                    .append(o),
                false, true
            ).toStringPretty();
        }
        // 返回null或者数据一律视为成功
        return CommonResult
                .success()
                .append(o);
    }
}
