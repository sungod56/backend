package org.Netflix.Exeptions;

public class DependancyFailureException extends RuntimeException{

    public DependancyFailureException(Throwable cause){

        super(cause);
    }

}
