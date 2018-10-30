package com.inno72.common;

public class Inno72BizException extends RuntimeException {
    public Inno72BizException(String msg){
        super(msg);
    }

    public Inno72BizException(){
        super();
    }
}
