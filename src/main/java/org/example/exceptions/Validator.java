package org.example.exceptions;

public class Validator {
    public static void validateException(String exceptionMessage) throws UniqueConstraintException {
        if (exceptionMessage.contains("UNIQUE")){
            throw new UniqueConstraintException();
        }
    }
}
