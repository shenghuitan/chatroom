package com.romtn.oio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.romtn.common.CharsetConfig;
import com.romtn.common.Print;

/**
 * 
 * @author Roman Tan
 * @since 2017-07-13
 *
 */
public class EchoOioClient implements Print {
    
    private long id = System.currentTimeMillis() % 1000;
    
    public void bootstrap() throws InterruptedException {
        Socket socket;
        try {
            socket = new Socket("127.0.0.1", 2000);
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int len;
                    byte[] bytes = new byte[1024];
                    
                    try {
                        while (!Thread.interrupted()){
                            while (bis.available() > 0 && (len = bis.read(bytes)) != -1){
                                String line = new String(bytes, 0, len, CharsetConfig.getCharset());
                                println("client:", id, " read line:", line);
                            }
                            Thread.sleep(1000);
                        }
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            
            byte[] bytes = new byte[1024];
            while (!Thread.interrupted()){
                int len;
                len = System.in.read(bytes);
                bos.write(bytes, 0, len);
                bos.flush();
            }
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException {
        new EchoOioClient().bootstrap();
    }
    
    
}
