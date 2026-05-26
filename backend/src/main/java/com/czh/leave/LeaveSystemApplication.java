package com.czh.leave;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.czh.leave.mapper")
public class LeaveSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaveSystemApplication.class, args);
    }

}
