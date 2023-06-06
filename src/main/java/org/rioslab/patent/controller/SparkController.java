package org.rioslab.patent.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.rioslab.patent.annot.CheckPackage;
import org.rioslab.patent.api.CommonResult;
import org.rioslab.patent.api.ResultCode;
import org.rioslab.patent.service.IPublicationsService;
import org.rioslab.patent.util.CacheUtil;
import org.rioslab.patent.util.ShellUtil;
import org.rioslab.patent.vo.*;
import org.rioslab.patent.vo.ExecDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
@Slf4j
public class SparkController {

    private static final String defaultCode = "package org.rioslab.spark.core.wc\n" +
            "import org.apache.spark.sql.functions.col\n" +
            "import org.apache.spark.{SparkConf, sql}\n" +
            "import org.apache.spark.sql.{Encoder, Encoders, SparkSession}\n" +
            "object WordCountSQL {\n" +
            "    // 这里是程序运行的主函数\n" +
            "    def run(args: Array[String]): String = {\n" +
            "        // 创建配置\n" +
            "        val config = new SparkConf() // 创建一个配置类的对象\n" +
            "            .setMaster(\"spark://mn1:6540\") // 设置spark的运行模式 local[*] 表示本地运行，自动确定使用的CPU核数\n" +
            "            .setAppName(\"WordCount SQL Application\") // 这里设置应用名\n" +
            "        // 这里创建一个Spark Session的对象，里面包含Spark Context，用于Spark运行时的操作\n" +
            "        val spark = SparkSession.builder().config(config).getOrCreate()\n" +
            "        // 这里导入将DataSet转换为DataFrame的一些工具类\n" +
            "        import spark.implicits._\n" +
            "        // 这里创建一个spark的DataFrame\n" +
            "        val df = spark\n" +
            "            .read // 表示读文件\n" +
            "            .option(\"header\", \"true\") // 设置参数header=true，表示有表头\n" +
            "            .option(\"multiline\", \"true\") // 设置参数multiline=true，表示一个单元格可能有多行\n" +
            "            // 使用\"来转义\"\n" +
            "            .option(\"escape\", \"\\\"\") // 设置escape=\"\\\"\"，表示使用双引号转义双引号。意思在csv文件里\"\"表示\"\n" +
            "            .csv(\"patent/patent_cleaned.csv\") // 读取csv文件\n" +
            "        df.show() // 向控制台打印Dataframe\n" +
            "        // 将Dataframe的每一行的第3列（摘要）第4列（描述），（从0开始计数）连接成一个字符串\n" +
            "        val lines = df.map(\n" +
            "            line => line(3).toString + \" \" + line(4).toString\n" +
            "        )\n" +
            "        val words = lines.flatMap(_.split(\" \")) // 根据空格拆分字符串成一个个的单词\n" +
            "        words.show()\n" +
            "        val wordsGroup = words.groupBy(\"value\") // 根据\"value\"这一个column分组\n" +
            "        val wordCount = wordsGroup\n" +
            "            .count() // 统计单词出现的频率\n" +
            "            .sort(col(\"count\").desc) // 根据count这一个column降序排列\n" +
            "        wordCount.show()\n" +
            "        // 不返回数据就返回null\n" +
            "        val str = wordCount.toJSON.collect().toString\n" +
            "        str\n" +
            "    }\n" +
            "}\n";

    @Autowired
    IPublicationsService pubService;

    @ApiOperation("提交Spark代码并编译")
    @PostMapping("/submit")
    @CheckPackage
    CommonResult<?> submitJob(@RequestParam("packageName") String packageName, @RequestParam("className") String className, @RequestBody SubmitJobVO body) {
        if ("WordCountSQL".equals(className) && "org.rioslab.spark.core.wc".equals(packageName)) {
            body.setCode(defaultCode);
        }
        String codeID = IdUtil.randomUUID();
        ExecDTO exec = ShellUtil.pack(packageName, className, body.getCode(), codeID);

        MavenCompileVO vo = new MavenCompileVO()
            .setExitCode(exec.getExit())
            .setOutput(exec.getOutput())
            .setCodeID(codeID)
            ;

        if (exec.getExit() == 0)
            return CommonResult.success().append(vo);
        else
            return CommonResult.fail(ResultCode.CompileError).append(vo);
    }



    @ApiOperation("执行Spark作业")
    @PostMapping("/run")
    CommonResult<?> runJob(@RequestParam("codeID") String codeID) {

        String taskID = IdUtil.randomUUID();
        ExecDTO exec = ShellUtil.run(taskID, codeID);

        log.info("Generate task id = " + taskID);

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
        JSONArray array = null;
        try {
            array = JSONUtil.parseArray(json);
        }
        catch (JSONException e) {
            log.error("Json解析错误");
            log.error(e.getMessage());
            return CommonResult.fail(ResultCode.JsonParseError);
        }
        return CommonResult.success().append(array);
    }

}
