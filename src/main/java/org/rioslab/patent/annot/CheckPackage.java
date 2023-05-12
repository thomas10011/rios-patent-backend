package org.rioslab.patent.annot;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckPackage {
    String[]args() default{""};
}