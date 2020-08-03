package com.sincosmos.rpc.rmi.client;

import com.sincosmos.rpc.rmi.api.AnotherRmiService;
import com.sincosmos.rpc.rmi.api.RmiService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RmiConsumer {
    public static void main(String[] args){
        try{
            RmiService service = (RmiService) Naming.lookup("rmi://localhost:9999/say-hello");
            System.out.println(service.sayHello("RMI"));

            AnotherRmiService anotherRmiService = (AnotherRmiService) Naming.lookup("rmi://localhost:9999/say-hi");
            System.out.println(anotherRmiService.sayHi("RMI"));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
