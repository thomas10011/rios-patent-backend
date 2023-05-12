package org.rioslab.patent.controller;


import cn.hutool.cache.Cache;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import org.json.JSONObject;
import org.rioslab.patent.api.CommonResult;
import org.rioslab.patent.entity.Publications;
import org.rioslab.patent.service.IPublicationsService;
import org.rioslab.patent.util.CacheUtil;
import org.rioslab.patent.util.ShellUtil;
import org.rioslab.patent.vo.PublicationsVO;
import org.rioslab.patent.vo.SparkExecuteVO;
import org.rioslab.patent.vo.SubmitJobVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

        String taskID = IdUtil.randomUUID();
        String output = ShellUtil.invoke(packageName, className, body.getCode(), taskID);

        boolean hashData = null == CacheUtil.getString(taskID);

        SparkExecuteVO vo = new SparkExecuteVO()
            .setTaskID(taskID)
            .setOutput(output)
            .setHasData(hashData)
            ;

        return CommonResult.success().append(vo);
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
