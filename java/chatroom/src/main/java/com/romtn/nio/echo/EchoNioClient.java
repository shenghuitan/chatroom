package com.romtn.nio.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class EchoNioClient implements Runnable {

    private SocketChannel socket;
    
    private ByteBuffer input = ByteBuffer.allocate(1024);
    private ByteBuffer output = ByteBuffer.allocate(1024);
    
    public EchoNioClient() {
        try {
            Selector selector = Selector.open();
            socket = SocketChannel.open();
            socket.configureBlocking(false);
            socket.connect(new InetSocketAddress("127.0.0.1", 2000));
            
            SelectionKey sk = socket.register(selector, 0);
            sk.interestOps(SelectionKey.OP_CONNECT);
            selector.wakeup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!Thread.interrupted()){
            byte[] bytes = new byte[1024];
            try {
                int len = System.in.read(bytes);
                
                output.put(bytes, 0, len);
                socket.write(output);
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
