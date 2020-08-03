package com.sincosmos.rpc.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AnotherRmiService extends Remote {
    String sayHi(String name) throws RemoteException;
}
