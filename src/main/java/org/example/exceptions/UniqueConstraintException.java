package org.example.exceptions;

public class UniqueConstraintException extends  Exception{
    String message="Unique Constraint Exception";
    @Override
    public String getMessage() {
        return message;
    }
}
