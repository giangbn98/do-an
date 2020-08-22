package com.hubt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class GbApplication {
    public static void main(String[] args) {
        SpringApplication.run(GbApplication.class, args);
    }
}
