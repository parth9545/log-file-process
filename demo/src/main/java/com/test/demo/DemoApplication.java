package com.test.demo;

import com.test.demo.service.LogFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private LogFileService logFileService;

    public static void main(String[] args) {
        new SpringApplication(DemoApplication.class).run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Log file processing started : ");
        if (logFileService.processLogFile(args)) {
            log.info("Log file processing completed successfully : ");
            logFileService.getAll();
        } else {
            log.info("Log file processing completed with some error : ");
        }
    }
}
