package org.example.repository;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {
    void create(T object) throws SQLException;

    List<T> getAll();

    void update(T object) throws SQLException;

    void delete(T object);
}
