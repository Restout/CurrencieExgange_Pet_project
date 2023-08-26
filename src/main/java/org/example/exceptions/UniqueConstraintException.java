package org.example.exceptions;

import java.sql.SQLException;

public class UniqueConstraintException extends SQLException {
    String message="Unique Constraint Exception";
    @Override
    public String getMessage() {
        return message;
    }
}
