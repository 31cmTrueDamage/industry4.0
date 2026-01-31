package org.industry40;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableScheduling
public class Machines {

    public static void main(String[] args) {
        SpringApplication.run(Machines.class, args);
    }
}
