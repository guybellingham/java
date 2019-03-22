package com.innotas.r2d2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * This is a java application that can accept <code>--args=</code> when run and 
 * in turn calls {@linkplain SpringApplication#run(Class, String...) which bootstraps our application, 
 * starting Spring, which, in turn, starts the auto-configured Tomcat web server passing along any 
 * command line args.
 * @author guybe
 *
 */
@SpringBootApplication
public class R2D2 {

    public static void main(String[] args) {
        SpringApplication.run(R2D2.class, args);
    }

}
