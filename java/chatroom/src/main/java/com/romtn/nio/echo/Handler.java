package com.romtn.nio.echo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import com.romtn.common.CharsetConfig;
import com.romtn.util.LogFormat;

public class Handler implements Runnable {

    private final SocketChannel socket;
    private final SelectionKey sk;
    
    private ByteBuffer input = ByteBuffer.allocate(1024);
    private ByteBuffer output = ByteBuffer.allocate(1024);
    
    static final int READING = 0, SENDING = 1;
    int state = READING;
    
    public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        socket = socketChannel;
        socket.configureBlocking(false);
        sk = socket.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        
        LogFormat.printAllKeys(selector);
        
        selector.wakeup();
    }

    private void read(){
        try {
            socket.read(input);
            
            process();
            
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void process(){
        input.flip();
        byte[] bytes = new byte[input.limit()];
        input.get(bytes);
        input.compact();
        
        String line = new String(bytes, CharsetConfig.getCharset());
        LogFormat.println("process line:", line);
        output.put(line.getBytes(CharsetConfig.getCharset()));
    }
    
    private void send(){
        try {
            output.flip();
            socket.write(output);
            output.compact();
            
            state = READING;
            sk.interestOps(SelectionKey.OP_READ);
//            sk.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void notify(String line){
        LogFormat.println("notify line:", line);
        output.put(line.getBytes(CharsetConfig.getCharset()));
        
        state = SENDING;
        sk.interestOps(SelectionKey.OP_WRITE);
        sk.selector().wakeup();
    }
    
    @Override
    public void run() {
        if (state == READING){
            read();
        }
        else if (state == SENDING){
            send();
        }
    }
    
}
