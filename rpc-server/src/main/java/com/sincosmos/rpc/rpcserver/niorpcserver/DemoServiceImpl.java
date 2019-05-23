package com.sincosmos.rpc.rpcserver.niorpcserver;

public class DemoServiceImpl implements DemoService{
    @Override
    public String sayHi(String name) {
        return "Hi, " + name;
    }
}
