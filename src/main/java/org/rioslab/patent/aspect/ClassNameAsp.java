package org.rioslab.patent.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.rioslab.patent.annot.CheckPackage;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@Slf4j
public class ClassNameAsp {

    @Pointcut("@annotation(org.rioslab.patent.annot.CheckPackage)")
    public void pointCut() {

    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) throws Throwable {
        log.info("Checking Spark Params.");
        Object[] args = joinPoint.getArgs();
        String packageName = args[0].toString();
        String className = args[1].toString();
        if (!packageName.startsWith("org.rioslab.spark.core.") || StringUtils.isEmpty(packageName) || StringUtils.isEmpty(className)) {
            args[0] = "org.rioslab.spark.core.wc";
            args[1] = "WordCountSQL";
        }
    }


}
