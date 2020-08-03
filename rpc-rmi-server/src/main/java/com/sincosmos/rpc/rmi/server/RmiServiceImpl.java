package com.sincosmos.rpc.rmi.server;

import com.sincosmos.rpc.rmi.api.RmiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiServiceImpl extends UnicastRemoteObject implements RmiService {
    protected RmiServiceImpl() throws RemoteException {
    }

    @Override
    public String sayHello(String name) throws RemoteException {
        return "Hello, " + name;
    }
}
