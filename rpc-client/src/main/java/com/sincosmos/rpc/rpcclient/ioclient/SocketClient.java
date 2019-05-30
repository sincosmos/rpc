package com.sincosmos.rpc.rpcclient.ioclient;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {
    public static void main(String[] args) {
        Runnable client = () -> {
            try {
                new SocketClient().startClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        new Thread(client, "client-A").start();
        new Thread(client, "client-B").start();
    }

    private void startClient() throws IOException {
        String host="localhost";
        int port = 4444;
        String tname = Thread.currentThread().getName();
        String[] messages = new String[]{ tname + " > msg1", tname + " > msg2", tname + " > msg3", "Done", tname + " > msg4"};
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), 500);
        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for(String msg : messages){
                BufferedReader stdIn = new BufferedReader(new StringReader(msg));
                String userInput;
                while((userInput = stdIn.readLine()) != null){
                    out.println(userInput);
                    System.out.println("echo: " + in.readLine());
                }
            }
            socket.shutdownInput();
            socket.shutdownOutput();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }finally {
            socket.close();
        }
    }
}
