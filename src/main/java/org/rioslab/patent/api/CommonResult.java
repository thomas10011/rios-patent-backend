package org.rioslab.patent.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author thomas
 * @version 1.0
 * @date 2020/12/19 22:20
 * 统一的返回结果
 **/
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    private Boolean success;
    private String error_code;
    private String error_message;
    private T data;

    public static <T> CommonResult<T> success() {
        return new CommonResult<T>()
                .setSuccess(true);
    }

    public static <T> CommonResult<T> fail(ResultCode resultCode) {
        return new CommonResult<T>()
                .setSuccess(false)
                .setError_code(resultCode.getError_code())
                .setError_message(resultCode.getError_message());
    }
    public static <T> CommonResult<T> fail(String error_message) {
        return new CommonResult<T>()
                .setSuccess(false)
                .setError_message(error_message);
    }

    public CommonResult<T> append(T data) {
        this.data = data;
        return this;
    }

}
