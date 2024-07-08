package com.bma.CloudFileStorage.exceptions;

public class IllegalFileNameException extends RuntimeException{
    public IllegalFileNameException(String msg){
        super(msg);
    }
}
