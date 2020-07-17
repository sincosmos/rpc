package com.sincosmos.rpc.rpcserver.niorpcserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioSocketServer {
    private static int port = 4444;
    private static Selector selector;

    static {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //非阻塞
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        /*while(true){
            System.out.println("waiting for connections...");
            SocketChannel sc = server.accept();
            if(sc != null){
                System.out.println("Incoming connection from: "
                        + sc.socket().getRemoteSocketAddress());
                buffer.clear();
                if(sc.read(buffer) > 0){
                    buffer.flip();
                    while(buffer.hasRemaining())
                        System.out.print((char) buffer.get());
                }
                sc.close();
            }
        }*/
        // serverSocketChannel is ready for connection
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("starting server on port >> " + port);
        while(true){
            // wait for ready events
            // ready for connection, ready for reading, ready for writing
            // if no events ready, blocks here to save cpu
            if(selector.select() == 0) continue;
            Set<SelectionKey> selectKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectKeys.iterator();
            while(iter.hasNext()){
                SelectionKey selectionKey = iter.next();
                if(selectionKey.isAcceptable()){
                    // previous registered serverSocketChannel is ready for connection
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    // after a client is connected, a new SocketChannel is returned
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    System.out.println("Connected by: " + client.socket().getRemoteSocketAddress());
                    // register the socketChannel between server and client to the selector
                    client.register(selector, SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    //System.out.println("channel ready for read >>" + client.socket().getRemoteSocketAddress());
                    ByteBuffer buffer = ByteBuffer.allocate(8);
                    // 当客户端不再传来数据（客户端关闭）时，必须关闭这里的 channel，否则每次 select 后，这个 channel 都会是可读状态
                    // channel.close() 内部会调用 selectionKey 的 cancel() 方法，将其从 selector 中取消注册
                    if(client.read(buffer) < 0) client.close();
                    buffer.flip();
                    while(buffer.hasRemaining()){
                        System.out.print((char)buffer.get());
                    }

                }else if(selectionKey.isWritable()){
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    client.write(ByteBuffer.wrap("Server sending message to client...\n".getBytes()));
                }
                // we have to remove the processed ready event from the "ready table"
                // the ready table which returned by calling of selector.selectedKeys() method will not remove
                // the "ready channel" returned by previous call of selector.selectedKeys() method
                iter.remove();
            }
        }
    }

}
