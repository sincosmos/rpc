package com.sincosmos.redis;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RedisClient {
    private String host = "localhost";
    private int port = 6379;
    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new RedisDecoder())
                            .addLast(new RedisBulkStringAggregator())
                            .addLast(new RedisArrayAggregator())
                            .addLast(new RedisEncoder())
                            .addLast(new RedisClientHandler());
                        }
                    });

            ChannelFuture f = b.connect(host, port).sync();
            //blocks until channel closed
            ChannelFuture sendFuture = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                try{
                    String cmd = br.readLine();
                    if("quit".equalsIgnoreCase(cmd)){
                        break;
                    }
                    sendFuture = f.channel().writeAndFlush(cmd);
                    sendFuture.addListener(send -> {
                        if(!send.isSuccess()){
                            System.err.println("send command failed");
                            send.cause().printStackTrace();
                        }else{
                            System.out.println("command sent: " + cmd);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(sendFuture != null){
                sendFuture.sync();
            }
            //f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args){
        RedisClient client = new RedisClient();
        client.start();
    }
}
