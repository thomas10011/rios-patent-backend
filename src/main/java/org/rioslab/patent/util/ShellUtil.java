package org.rioslab.patent.util;

import lombok.extern.slf4j.Slf4j;
import org.rioslab.patent.vo.ExecDTO;

import java.io.*;

@Slf4j
public class ShellUtil {

    public static void test(String[] args) {
        String[] arr = { "ls" };
        execute(arr, "/Users/thomas/Projects");

    }

    public static String mainCode = "package org.rioslab.spark.core\n" +
        "import org.rioslab.spark.core.util.CacheUtil\n" +
        "import PACKAGE\n" +
        "\n" +
        "object App {\n" +
        "    def main(args: Array[String]): Unit = {\n" +
        "        // 0号参数作为 task id\n" +
        "        val taskID = args(0)\n" +
        "        val res = APPLICATION.run(args)\n" +
        "        if (res != null && res.nonEmpty) CacheUtil.set(taskID, res)\n" +
        "    }\n" +
        "}"
        ;

    public static ExecDTO pack(String packageName, String className, String code, String codeID) {
        // 如果其中一个为空，执行默认的程序
        ExecDTO res = new ExecDTO();
        if (writeMain(packageName, className, codeID)) {
            log.error("writeMain function return false!");
            return  res;
        }
        if (writeCode(packageName, className, code, codeID)) {
            String[] arr = { "mvn", "package"};
            res = execute(arr, "/work/stu/hrtan/projects/rios-patent-execute/");
        }
        else {
            res.setExit(10086).setOutput("Error occurred when write code");
        }

        log.info("Maven package output.");
        System.out.println(res.getOutput());

        return res;
    }


    public static ExecDTO run(String taskID, String codeID) {
        return submit(taskID, codeID);
    }

    private static boolean writeMain(String packageName, String className, String codeID) {
        File mainFile = new File("/tmp/patent/" + codeID + "/rios-patent-execute/src/main/java/App.scala");

        // 写入代码
        try {
            if (!mainFile.exists() && !mainFile.createNewFile()) {
                log.error("Failed to create file for code " + codeID + "!");
                return false;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(mainFile));
            writer.write(mainCode.replace("PACKAGE", packageName + "." + className).replace("APPLICATION", className));
            writer.close();
        }
        catch (IOException e) {
            log.error("Exception occurred when write code main file.");
            return false;
        }
        return true;
    }

    private static boolean writeCode(String packageName, String className, String code, String codeID) {
        String dirStr = "/tmp/patent/"+ codeID + "/rios-patent-execute/src/main/java/" +  packageName.replace(".", "/");

        // 创建目录以及文件
        File dir = new File(dirStr);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                log.error("Failed when create code directory.");
                return false;
            }
        }

        String filePath = dirStr + "/" + className + ".scala";
        File codeFile = new File(filePath);
        if (!codeFile.exists()) {
            try {
                if (!codeFile.createNewFile()) {
                    log.error("Failed when create code file.");
                    return false;
                }
            }
            catch (IOException e) {
                log.error("Exception occurred when create code file.");
                return false;
            }
        }

        // 写入代码
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(codeFile));
            writer.write(code);
            writer.close();
        }
        catch (IOException e) {
            log.error("Exception occurred when write code into file.");
            return false;
        }

        return true;
    }


    private static ExecDTO submit(String taskID, String codeID) {
        String[] arr = {
            "/work/stu/hrtan/spark-submit.sh",
            taskID,
            codeID
        };
        return execute(arr, null);
    }


    public static ExecDTO execute(String[] cmdArr, String workDir) {

        ProcessBuilder builder = new ProcessBuilder();
        ExecDTO ret = new ExecDTO();

        if (workDir != null && !workDir.isEmpty()) {
            builder.directory(new File(workDir));
        }
        builder.command(cmdArr);
        builder.redirectError(new File("/work/stu/hrtan/patent_error.log"));

        StringBuilder output = new StringBuilder();
        try {

            final Process proc = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
                System.out.flush(); // 立即打印输出
            }

            int exit = proc.waitFor();
            ret.setExit(exit);

            log.info("Command execute exited with code: " + exit);
            proc.destroy();

        }
        catch (Exception e) {
            log.error("Exception occurred while Launching process: " + e.getMessage());
        }

        ret.setOutput(output.toString());
        return ret;
    }


}
