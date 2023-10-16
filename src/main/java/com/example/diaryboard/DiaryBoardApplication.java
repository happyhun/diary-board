package com.example.diaryboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DiaryBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiaryBoardApplication.class, args);
    }

}
