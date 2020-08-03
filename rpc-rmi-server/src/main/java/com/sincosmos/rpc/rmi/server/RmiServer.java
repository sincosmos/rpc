package com.sincosmos.rpc.rmi.server;

import com.sincosmos.rpc.rmi.api.AnotherRmiService;
import com.sincosmos.rpc.rmi.api.RmiService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiServer {
    public static void main(String[] args){
        try{
            RmiService service = new RmiServiceImpl();
            AnotherRmiService anotherRmiService = new AnotherRmiServiceImpl();
            Registry registry = LocateRegistry.createRegistry(9999);
            //registry.bind(RmiService.class.getName(), service);
            Naming.rebind("rmi://127.0.0.1:9999/say-hello", service);
            Naming.rebind("rmi://127.0.0.1:9999/say-hi", anotherRmiService);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("----server started----");
    }
}
