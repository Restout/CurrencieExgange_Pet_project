package org.example.repository;

import org.example.DataSource;
import org.sqlite.SQLiteDataSource;

import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {
    SQLiteDataSource dataSource = DataSource.getDataSource();
    void create(T object) throws SQLException;

    List<T> getAll();

    void update(T object) throws SQLException;

    void delete(T object);
}
