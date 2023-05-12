package org.rioslab.patent.api;

import lombok.Getter;

/**
 * @author thomas
 * @version 1.0
 * @date 2020/12/19 21:36 下午
 * 状态码
 */
@Getter
public enum ResultCode {

    /**
     * @date 2020/12/19 22:16
     * 错误码定义
     **/
    BadRequest("400", "请求有误！"),
    Unauthorized("401", "用户未授权！"),
    NotFound("404", "找不到请求地址！"),
    InternalServerError("500", "内部服务器错误！"),

    CompileError("10086", "Maven编译打包差出错！"),
    RunSparkError("10087", "Spark运行出错！")

    ;

    private final String error_code;

    private final String error_message;

    ResultCode(String errorCode, String errorMessage) {
        this.error_code = errorCode;
        this.error_message = errorMessage;
    }
}
