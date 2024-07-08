package com.bma.CloudFileStorage.exceptions;

public class IllegalFolderNameException extends RuntimeException{
    public IllegalFolderNameException(String msg){
        super(msg);
    }
}
