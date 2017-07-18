package com.romtn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.romtn.common.Print;

public class EchoNioServer implements Runnable, Print {

    private Selector selector;
    private ServerSocketChannel serverSocket;
    private Handlers handlers = new Handlers();
    
    public EchoNioServer(){
        try {
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(2000));
            serverSocket.configureBlocking(false);
            SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            
            sk.attach(new Acceptor(serverSocket, selector, handlers));
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()){
                    new Thread(new Notifier(handlers, new Date().toString())).start();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        
    }
    
    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                printAllKeys(selector);
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                for (Iterator<SelectionKey> iter = selected.iterator(); iter.hasNext();){
                    SelectionKey sk = iter.next();
                    iter.remove();
                    
                    int ops = sk.interestOps();
                    println("attachment:", sk.attachment(), ", interestOps:", ops);
                    
                    Runnable r = (Runnable) sk.attachment();
                    if (r != null){
                        r.run();
                    }
                }
//                selected.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public static void main(String[] args) {
        new Thread(new EchoNioServer()).start();
    }
    
}
