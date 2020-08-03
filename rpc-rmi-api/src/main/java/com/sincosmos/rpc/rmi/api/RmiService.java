package com.sincosmos.rpc.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RmiService extends Remote {
    String sayHello(String name) throws RemoteException;
}
