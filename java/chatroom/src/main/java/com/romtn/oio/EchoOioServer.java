package com.romtn.oio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.romtn.common.CharsetConfig;
import com.romtn.common.Print;

/**
 * 
 * @author Roman Tan
 * @since 2017-07-13
 *
 */
public class EchoOioServer implements Print {
    
    private Map<Socket, Object> socketMap = new ConcurrentHashMap<>();
    
    public void bootstrap(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2000);
            println("serverSocket address:", serverSocket.getInetAddress(), ", port:", serverSocket.getLocalPort());
            
            while (!Thread.interrupted()){
                ThreadGroup threadGroup = new ThreadGroup("server thread group");
                final Socket socket = serverSocket.accept();
                socketMap.put(socket, new Object());
                
                Thread thread = new Thread(threadGroup, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            
                            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                            
                            byte[] bytes = new byte[1024];
                            
                            int len;
                            String line;
                            while ((len = bis.read(bytes)) != -1){
                                line = new String(bytes, 0, len, CharsetConfig.getCharset());
                                println("server read line:", line);
                            
                                for (Socket socket : socketMap.keySet()){
                                    if (socket.isClosed()){
                                        socketMap.remove(socket);
                                        continue;
                                    }
                                    BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                                    bos.write(line.getBytes(CharsetConfig.getCharset()));
                                    bos.flush();
                                }
                                
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null){
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    public static void main(String[] args) {
        new EchoOioServer().bootstrap();;
    }
    
}
