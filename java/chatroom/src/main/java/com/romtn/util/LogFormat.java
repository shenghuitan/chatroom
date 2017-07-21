package com.romtn.util;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.apache.commons.lang3.StringUtils;

public class LogFormat {
    
    public static String println(Object...objects){
        return StringUtils.join(objects);
    }
    
    public static String printAllKeys(Selector selector){
        StringBuilder builder = new StringBuilder();
        for (SelectionKey sk : selector.keys()){
            if (!sk.isValid()){
                builder.append("invalid sk:").append(sk).append('\n');
                continue;
            }
            builder.append("sk.attachment:").append(sk.attachment()).append('\n');
            builder.append("sk.interestOps:").append(sk.interestOps()).append('\n');
        }
        return builder.toString();
    }
    
}
