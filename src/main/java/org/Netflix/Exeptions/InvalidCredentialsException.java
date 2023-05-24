package org.Netflix.Exeptions;

public class InvalidCredentialsException extends RuntimeException{
    private String message;
    public InvalidCredentialsException(final String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;

    }
}
