package org.rioslab.patent.controller;


import cn.hutool.cache.Cache;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.rioslab.patent.api.CommonResult;
import org.rioslab.patent.api.ResultCode;
import org.rioslab.patent.entity.Publications;
import org.rioslab.patent.service.IPublicationsService;
import org.rioslab.patent.util.CacheUtil;
import org.rioslab.patent.util.ShellUtil;
import org.rioslab.patent.vo.*;
import org.rioslab.patent.vo.ExecDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author thomas
 * @since 2023-04-28
 */
@RestController
@RequestMapping("/patent/publications")
@CrossOrigin
public class SparkController {

    @Autowired
    IPublicationsService pubService;

    @ApiOperation("提交Spark作业")
    @PostMapping("/submit")
    CommonResult<?> submitJob(@RequestParam("packageName") String packageName, @RequestParam("className") String className, @RequestBody SubmitJobVO body) {

        ExecDTO exec = ShellUtil.pack(packageName, className, body.getCode());

        MavenCompileVO vo = new MavenCompileVO()
            .setExitCode(exec.getExit())
            .setOutput(exec.getOutput())
            ;

        if (exec.getExit() == 0)
            return CommonResult.success().append(vo);
        else
            return CommonResult.fail(ResultCode.CompileError).append(vo);
    }



    @ApiOperation("执行Spark作业")
    @PostMapping("/run")
    CommonResult<?> runJob(@RequestParam("packageName") String packageName, @RequestParam("className") String className) {

        String taskID = IdUtil.randomUUID();
        ExecDTO exec = ShellUtil.run(packageName, className, taskID);

        boolean hashData = null != CacheUtil.getString(taskID);

        SparkExecuteVO vo = new SparkExecuteVO()
                .setTaskID(taskID)
                .setOutput(exec.getOutput())
                .setHasData(hashData)
                .setExitCode(exec.getExit())
                ;

        if (exec.getExit() == 0)
            return CommonResult.success().append(vo);
        else
            return CommonResult.fail(ResultCode.RunSparkError).append(vo);
    }


    @ApiOperation("根据Task ID查询Spark作业的输出结果")
    @GetMapping("/query")
    CommonResult<?> queryJobResult(@RequestParam("taskID") String taskID) {
        String json = CacheUtil.getString(taskID);
        if (null == json) return CommonResult.success().append("");
        JSONArray array = JSONUtil.parseArray(json);
        return CommonResult.success().append(array);
    }

}
