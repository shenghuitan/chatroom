package com.romtn.common;

import java.nio.charset.Charset;

public class CharsetConfig {

    private static final Charset CHARSET = Charset.forName("utf-8");
    
    public static Charset getCharset(){
        return CHARSET;
    }
    
}
