package org.rioslab.patent.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;

@Slf4j
public class ShellUtil {

    public static void test(String[] args) {
        String[] arr = { "ls" };
        execute(arr, "/Users/thomas/Projects");

    }

    public static String mainCode = "package org.rioslab.spark.core\n" +
        "import org.rioslab.spark.core.util.CacheUtil\n" +
        "import package\n" +
        "\n" +
        "object App {\n" +
        "    def main(args: Array[String]): Unit = {\n" +
        "        // 0号参数作为 task id\n" +
        "        val taskID = args(0)\n" +
        "        val res = application.run(args)\n" +
        "        if (res != null && res.nonEmpty) CacheUtil.set(taskID, res)\n" +
        "    }\n" +
        "}"
        ;

    public static String invoke(String packageName, String className, String code, String taskID) {
        // 如果其中一个为空，执行默认的程序
        if (StringUtils.isEmpty(packageName) || StringUtils.isEmpty(className)) {
            writeMain("org.rioslab.spark.core.wc", "WordCountSQL");
        }
        else {
            writeMain(packageName, className);
            writeCode(packageName, className, code);
        }
        log.info("Maven package output.");
        System.out.println(pack());
        return submit(taskID);
    }

    public static boolean writeMain(String packageName, String className) {
        File mainFile = new File("/work/stu/hrtan/projects/rios-patent-execute/src/main/java/App.scala");
        // 写入代码
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(mainFile));
            writer.write(mainCode.replace("package", packageName + "." + className).replace("application", className));
            writer.close();
        }
        catch (IOException e) {
            log.error("Exception occurred when write code main file.");
            return false;
        }
        return true;
    }

    public static boolean writeCode(String code, String packageName, String className) {
        String dirStr = "/work/stu/hrtan/projects/rios-patent-execute/src/main/java/" +  packageName.replace(".", "/");

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

    public static String pack() {
        String[] arr = { "mvn", "package"};
        return execute(arr, "/work/stu/hrtan/projects/rios-patent-execute/");
    }

    public static String submit(String taskID) {
        String[] arr = {
            "spark-submit",
            "--class",
            "org.rioslab.spark.core.App",
            "--master",
            "spark://mn1",
            "/work/stu/hrtan/projects/rios-patent-execute/target/rios-patent-execute-1.0-SNAPSHOT.jar",
            taskID
        };
        return execute(arr, null);
    }


    public static String execute(String[] cmdArr, String workDir) {

        ProcessBuilder builder = new ProcessBuilder();

        if (workDir != null && !workDir.isEmpty()) {
            builder.directory(new File(workDir));
        }
        builder.command(cmdArr);

        StringBuilder output = new StringBuilder();
        try {

            final Process proc = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
            }

            int exit = proc.waitFor();

            log.info("Command execute exited with code: " + exit);
            proc.destroy();

        }
        catch (Exception e) {
            log.error("Exception occurred while Launching process: " + e.getMessage());
        }

        return output.toString();
    }


}
