package com.romtn.nio.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.romtn.common.CharsetConfig;

public class EchoNioClient implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(EchoNioClient.class);
    
    private Selector selector;
    
    private ByteBuffer input = ByteBuffer.allocate(1024);
    private ByteBuffer output = ByteBuffer.allocate(1024);
    
    public EchoNioClient() {
        try {
            selector = Selector.open();
            SocketChannel socket = SocketChannel.open();
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
            try {
                selector.select();
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()){
                    SelectionKey sk = iter.next();
                    iter.remove();
                    
                    if (!sk.isValid()){
                        continue;
                    }
                    
                    if (sk.isConnectable()){
                        connect(sk);
                    }
                    else if (sk.isReadable()){
                        read(sk);
                    }
                    else if (sk.isWritable()){
                        write(sk);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
    }
    
    private void connect(SelectionKey sk){
        try {
            SocketChannel socketChannel = (SocketChannel) sk.channel();
            socketChannel.finishConnect();
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
        sk.interestOps(SelectionKey.OP_WRITE);
    }
    
    private void read(SelectionKey sk){
        try {
            SocketChannel socketChannel = (SocketChannel) sk.channel();
            socketChannel.read(input);
            
            input.flip();
            byte[] bytes = new byte[input.limit()];
            input.get(bytes);

            input.clear();
            
            logger.info("read:{}", new String(bytes, CharsetConfig.getCharset()));
            
            sk.interestOps(SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void write(SelectionKey sk){
        byte[] bytes = new byte[1024];
        try {
            SocketChannel socketChannel = (SocketChannel) sk.channel();
            int len = System.in.read(bytes);
            logger.info("write:{}", new String(bytes, 0, len, CharsetConfig.getCharset()));
            
            output.put(bytes, 0, len);
            output.flip();
            socketChannel.write(output);
            
            output.clear();
            
            sk.interestOps(SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new Thread(new EchoNioClient()).start();
    }
    
}
