package com.sincosmos.redis.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LettuceClient {
    public static void main(String[] args){
        RedisURI uri = RedisURI.builder().withHost("localhost").withPort(6379).build();
        RedisClient client = RedisClient.create(uri);
        StatefulRedisConnection<String, String> conn = client.connect();
        RedisCommands<String, String> commands = conn.sync();
        SetArgs setArgs = SetArgs.Builder.nx().ex(60);
        String result = commands.set("name", "throwable", setArgs);

        log.info("Set: " + result);

        result = commands.get("name");

        log.info("Get: " + result);
        conn.close();
        client.shutdown();
    }
}
