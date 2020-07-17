package com.sincosmos.netty;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class NettyClientApplication {
    public static void main(String[] args){
        SpringApplication.run(NettyClientApplication.class, args);
    }
}
