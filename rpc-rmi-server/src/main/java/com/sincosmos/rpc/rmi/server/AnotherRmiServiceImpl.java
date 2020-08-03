package com.sincosmos.rpc.rmi.server;

import com.sincosmos.rpc.rmi.api.AnotherRmiService;
import com.sincosmos.rpc.rmi.api.RmiService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AnotherRmiServiceImpl extends UnicastRemoteObject implements AnotherRmiService {
    protected AnotherRmiServiceImpl() throws RemoteException {
    }

    @Override
    public String sayHi(String name) throws RemoteException {
        return "Hi, " + name;
    }
}
