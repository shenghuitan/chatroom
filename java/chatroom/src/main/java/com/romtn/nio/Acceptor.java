package com.romtn.nio;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.romtn.common.Print;

public class Acceptor implements Runnable, Print {

    private final Selector selector;
    private final ServerSocketChannel serverSocket;
    private Handlers handlers;
    
    public Acceptor(ServerSocketChannel serverSocket, Selector selector, Handlers handlers) {
        this.serverSocket = serverSocket;
        this.selector = selector;
        this.handlers = handlers;
    }
    
    @Override
    public void run() {
        println("acceptor");
        try {
            SocketChannel c = serverSocket.accept();
            if (c != null){
                handlers.register(new Handler(selector, c));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
