package com.romtn.common;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.apache.commons.lang3.StringUtils;

public interface Print {
    
    public default void println(Object...objects){
        System.out.println(StringUtils.join(objects));
    }
    
    public default void printAllKeys(Selector selector){
        for (SelectionKey sk : selector.keys()){
            if (!sk.isValid()){
                println("sk:", sk);
                continue;
            }
            println("sk.attachment:", sk.attachment());
            println("sk.interestOps:", sk.interestOps());
            
        }
    }
    
}
