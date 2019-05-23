package com.sincosmos.rpc.common.nio;

import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Utils {
    public void readFile(String file){
        try {
            FileChannel fc = new FileInputStream(ResourceUtils.getFile(file)).getChannel();
            ByteBuffer buff = ByteBuffer.allocate(4);
            while(fc.read(buff) > 0){
                buff.flip();
                while(buff.hasRemaining())
                    System.out.print((char)buff.get());
                buff.clear();
            }
            fc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        Utils utils = new Utils();
        utils.readFile("classpath:setting.properties");
    }
}
