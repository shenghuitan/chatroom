package com.romtn.nio;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Handlers {

    private Map<Runnable, Object> handlers = new ConcurrentHashMap<>();
    
    public void register(Runnable r){
        handlers.put(r, new Object());
    }
    
    public void remove(Runnable r){
        handlers.remove(r);
    }
    
    public Set<Runnable> getHandlers(){
        return handlers.keySet();
    }
   
    
}
