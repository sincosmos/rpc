package com.sincosmos.rpc.rpcclient.nioclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioSocketClient {
    public static void main(String[] args){
        String host="localhost";
        int port = 4444;
        SocketChannel client = null;
        try {
            client = SocketChannel.open(new InetSocketAddress(host, port));
            client.write(ByteBuffer.wrap("Client sending message to server...\n".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
