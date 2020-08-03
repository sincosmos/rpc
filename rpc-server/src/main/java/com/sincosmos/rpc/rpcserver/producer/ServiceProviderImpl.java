package com.sincosmos.rpc.rpcserver.producer;

public class ServiceProviderImpl implements ServiceProvider{

    @Override
    public String getService(String name) {
        return "Hello, " + name;
    }
}
