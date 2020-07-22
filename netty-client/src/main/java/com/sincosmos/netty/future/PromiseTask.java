package com.sincosmos.netty.future;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.ImmediateEventExecutor;
import io.netty.util.concurrent.Promise;

import java.util.concurrent.TimeUnit;

public class PromiseTask {
    public static void main(String[] args){
        PromiseTask task = new PromiseTask();
        Promise<String> promise = task.doBusiness("Laugh");
        promise.addListener(future -> {
            System.out.println(future.get() + ", done");
            future.addListener(f -> System.out.println("nested listener notified"));
        });
        System.out.println("Main done");
    }

    private Promise<String> doBusiness(String value){
        NioEventLoopGroup executor = new NioEventLoopGroup();
        DefaultPromise<String> promise = new DefaultPromise<>(ImmediateEventExecutor.INSTANCE);
        executor.schedule(()->{
            try{
                //do business logic
                TimeUnit.SECONDS.sleep(1);
                promise.setSuccess(value + " success");
            }catch (InterruptedException e){
                e.printStackTrace();
                promise.setFailure(e);
            }
            return promise;
        }, 0, TimeUnit.SECONDS);
        executor.shutdownGracefully();
        return promise;
    }
}
