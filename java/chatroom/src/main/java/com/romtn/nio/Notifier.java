package com.romtn.nio;

public class Notifier implements Runnable {

    private Handlers handlers;
    private String content;
    
    public Notifier(Handlers handlers, String content) {
        this.handlers = handlers;
        this.content = content;
    }

    @Override
    public void run() {
        for (Runnable r : handlers.getHandlers()){
            if (!(r instanceof Handler)){
                handlers.remove(r);
                continue;
            }
            Handler h = (Handler) r;
            h.notify(content);
        }
    }
    
}
