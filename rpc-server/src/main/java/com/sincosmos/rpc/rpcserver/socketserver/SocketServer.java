package com.sincosmos.rpc.rpcserver.socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @see <a href='https://medium.com/coderscorner/tale-of-client-server-and-socket-a6ef54a74763'>
 *     Blocking I/O and non-blocking I/O</a>
 */
public class SocketServer {
    public static void main(String[] args){
        int port = 4444;
        System.out.println("Waiting on port: " + port + "...");
        boolean listening = true;
        int i = 1;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(listening){
                /* Wait for the client to make a connection and when it does,
                create a new socket to handle the request */
                Socket socket = serverSocket.accept();
                System.out.println(i++ + "connected");

                try{
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    String request, response;
                    while((request=in.readLine()) != null){
                        response = processRequest(request);
                        out.println(response);
                        if("Done".equals(request)) break;
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Handle each connection in a new thread to manage concurrent users
                /*new Thread(()->{
                    try{
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                        String request, response;
                        while((request=in.readLine()) != null){
                            response = processRequest(request);
                            out.println(response);
                            if("Done".equals(request)) break;
                        }
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();*/
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String processRequest(String request) {
        System.out.println("Server receive message from > " + request);
        return "Server response: " + request;
    }
}
