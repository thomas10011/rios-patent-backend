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

    public static String utilCode = "package org.rioslab.spark.core.util;\n" +
            "\n" +
            "import redis.clients.jedis.Jedis;\n" +
            "import redis.clients.jedis.JedisPool;\n" +
            "\n" +
            "public class CacheUtil {\n" +
            "\n" +
            "    public static JedisPool pool = new JedisPool(\"localhost\", 6379);\n" +
            "\n" +
            "    public static boolean set(String key, String value) {\n" +
            "        try (Jedis jedis = pool.getResource()) {\n" +
            "            jedis.set(key, value);\n" +
            "        }\n" +
            "        catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            return false;\n" +
            "        }\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    public static boolean del(String key) {\n" +
            "        try (Jedis jedis = pool.getResource()) {\n" +
            "            jedis.del(key);\n" +
            "        }\n" +
            "        catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "            return false;\n" +
            "        }\n" +
            "        return true;\n" +
            "    }\n" +
            "\n" +
            "    public static String getString(String key) {\n" +
            "        String ret = \"\";\n" +
            "        try (Jedis jedis = pool.getResource()) {\n" +
            "            ret = jedis.get(key);\n" +
            "        }\n" +
            "        catch (Exception e) {\n" +
            "            e.printStackTrace();\n" +
            "        }\n" +
            "        return ret;\n" +
            "    }\n" +
            "\n" +
            "}"
            ;

    public static String pomCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n" +
            "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
            "    <modelVersion>4.0.0</modelVersion>\n" +
            "\n" +
            "    <groupId>riolab.org</groupId>\n" +
            "    <artifactId>rios-patent-execute</artifactId>\n" +
            "    <version>1.0-SNAPSHOT</version>\n" +
            "\n" +
            "    <properties>\n" +
            "        <spark.version>3.2.4</spark.version>\n" +
            "        <jedis.version>4.3.0</jedis.version>\n" +
            "\n" +
            "        <maven.compiler.source>8</maven.compiler.source>\n" +
            "        <maven.compiler.target>8</maven.compiler.target>\n" +
            "        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n" +
            "    </properties>\n" +
            "\n" +
            "\n" +
            "    <dependencies>\n" +
            "        <dependency>\n" +
            "            <groupId>org.apache.spark</groupId>\n" +
            "            <artifactId>spark-core_2.13</artifactId>\n" +
            "            <version>${spark.version}</version>\n" +
            "        </dependency>\n" +
            "        <dependency>\n" +
            "            <groupId>org.apache.spark</groupId>\n" +
            "            <artifactId>spark-sql_2.13</artifactId>\n" +
            "            <version>${spark.version}</version>\n" +
            "        </dependency>\n" +
            "        <!-- Redis 连接 -->\n" +
            "        <dependency>\n" +
            "            <groupId>redis.clients</groupId>\n" +
            "            <artifactId>jedis</artifactId>\n" +
            "            <version>${jedis.version}</version>\n" +
            "        </dependency>\n" +
            "    </dependencies>\n" +
            "\n" +
            "\n" +
            "    <build>\n" +
            "        <plugins>\n" +
            "            <!-- A plugin for compiling Java code -->\n" +
            "            <plugin>\n" +
            "                <groupId>org.apache.maven.plugins</groupId>\n" +
            "                <artifactId>maven-compiler-plugin</artifactId>\n" +
            "                <version>3.8.1</version>\n" +
            "                <configuration>\n" +
            "                    <source>1.8</source>\n" +
            "                    <target>1.8</target>\n" +
            "                </configuration>\n" +
            "            </plugin>\n" +
            "            <!-- Or, a plugin for compiling Scala code -->\n" +
            "            <!-- Make sure you are not using \"maven-scala-plugin\", which is the older version -->\n" +
            "            <plugin>\n" +
            "                <groupId>net.alchim31.maven</groupId>\n" +
            "                <artifactId>scala-maven-plugin</artifactId>\n" +
            "                <version>4.2.4</version>\n" +
            "                <executions>\n" +
            "                    <execution>\n" +
            "                        <goals>\n" +
            "                            <goal>compile</goal>\n" +
            "                        </goals>\n" +
            "                    </execution>\n" +
            "                </executions>\n" +
            "            </plugin>\n" +
            "            <!-- A plugin for creating an assembly JAR file (Java or Scala) -->\n" +
            "            <plugin>\n" +
            "                <groupId>org.apache.maven.plugins</groupId>\n" +
            "                <artifactId>maven-shade-plugin</artifactId>\n" +
            "                <version>3.2.1</version>\n" +
            "                <executions>\n" +
            "                    <execution>\n" +
            "                        <phase>package</phase>\n" +
            "                        <goals>\n" +
            "                            <goal>shade</goal>\n" +
            "                        </goals>\n" +
            "                        <configuration>\n" +
            "                            <filters>\n" +
            "                                <filter>\n" +
            "                                    <artifact>*:*</artifact>\n" +
            "                                    <excludes>\n" +
            "                                        <exclude>META-INF/*.SF</exclude>\n" +
            "                                        <exclude>META-INF/*.DSA</exclude>\n" +
            "                                        <exclude>META-INF/*.RSA</exclude>\n" +
            "                                    </excludes>\n" +
            "                                </filter>\n" +
            "                            </filters>\n" +
            "                        </configuration>\n" +
            "                    </execution>\n" +
            "                </executions>\n" +
            "            </plugin>\n" +
            "        </plugins>\n" +
            "    </build>\n" +
            "</project>"
            ;

    public static ExecDTO pack(String packageName, String className, String code, String codeID) {
        // 如果其中一个为空，执行默认的程序
        ExecDTO res = new ExecDTO();
        if (writeMain(packageName, className, codeID) && writeCode(packageName, className, code, codeID)) {
            String[] arr = { "mvn", "package"};
            res = execute(arr, "/tmp/patent/" + codeID + "/rios-patent-execute/");
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

    private static boolean writeFile(String dirStr, String fileStr, String content) {
        // 创建目录以及文件
        File dir = new File(dirStr);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                log.error("Failed when create directory = " + dirStr);
                return false;
            }
        }

        String filePath = dirStr + fileStr;

        File codeFile = new File(filePath);
        if (!codeFile.exists()) {
            try {
                if (!codeFile.createNewFile()) {
                    log.error("Failed when create file = " + filePath);
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
            writer.write(content);
            writer.close();
        }
        catch (IOException e) {
            log.error("Exception occurred when write code into file.");
            return false;
        }

        return true;
    }

    private static boolean writeMain(String packageName, String className, String codeID) {
        String mainDir = "/tmp/patent/"+ codeID + "/rios-patent-execute/src/main/java/";
        String pomDir = "/tmp/patent/"+ codeID + "/rios-patent-execute/";
        String utilDir = "/tmp/patent/"+ codeID + "/rios-patent-execute/src/main/java/org/rioslab/spark/core/util/";
        String code = mainCode.replace("PACKAGE", packageName + "." + className).replace("APPLICATION", className);

        // 写入代码
        return writeFile(pomDir, "pom.xml", pomCode) && writeFile(mainDir, className + ".scala", code) && writeFile(utilDir, "CacheUtil.java", utilCode);
    }

    private static boolean writeCode(String packageName, String className, String code, String codeID) {
        String dirStr = "/tmp/patent/"+ codeID + "/rios-patent-execute/src/main/java/" +  packageName.replace(".", "/");

        return writeFile(dirStr, className + ".scala", code);
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
