package org.example.exceptions;

import java.sql.SQLException;

public class Validator {
    public static void validateException(String exceptionMessage) throws SQLException {
        if (exceptionMessage.contains("UNIQUE")) {
            throw new UniqueConstraintException();
        }
        throw new SQLException(exceptionMessage);
    }
}
