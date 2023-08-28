package org.example.exceptions;

import java.sql.SQLException;

public class EntityNotFoundException extends SQLException {
    String message = "Entity not exist in database";

    @Override
    public String getMessage() {
        return message;
    }
}
