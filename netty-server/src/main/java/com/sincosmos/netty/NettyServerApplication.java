package com.sincosmos.netty;

import com.sincosmos.netty.server.EchoServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
@SpringBootApplication
public class NettyServerApplication {
    public static void main(String[] args){
        SpringApplication.run(NettyServerApplication.class, args);
    }

    private final EchoServer echoServer;

    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener(){
        return applicationReadyEvent -> {
            try {
                echoServer.start();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
    }
}
