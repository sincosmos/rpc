package com.sincosmos.rpc.rpcserver.niorpcserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceCenter implements Server {
    private static ExecutorService executor
            = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final ConcurrentHashMap<String, Class> serviceRegistry
            = new ConcurrentHashMap<>();
    private static boolean running = false;
    private static int port;
    private static Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public ServiceCenter(int port){
        this.port = port;
        try{
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        //非阻塞
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(this.port));
        System.out.println("starting server...");
        while(true){
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
        }

        /*server.register(selector, SelectionKey.OP_ACCEPT);
        while(selector.select() > 0){
            Set<SelectionKey> selectKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectKeys.iterator();
            while(iter.hasNext()){
                SelectionKey selectionKey = iter.next();
                if(selectionKey.isReadable()){

                }
            }
        }*/
    }

    @Override
    public void stop() {
        running = false;
        executor.shutdown();
    }

    @Override
    public void register(Class serviceInterface, Class impl) {

    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPort() {
        return port;
    }

    private static class ServerTask implements Runnable{

        SocketChannel client = null;
        public ServerTask(SocketChannel client){
            this.client = client;
        }
        @Override
        public void run() {

        }
    }
}
