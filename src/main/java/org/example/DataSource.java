package org.example;

import org.sqlite.SQLiteDataSource;

public class DataSource {
    public static SQLiteDataSource getDataSource(){
        SQLiteDataSource ds = new SQLiteDataSource();
         //ds.setUrl("jdbc:sqlite:D:\\SQLite\\sqlite-tools-win32-x86-3420000\\SQLiteDataBasesCurExch.db");
        ds.setUrl("jdbc:sqlite::resource:SQLiteDataBasesCurExch.db");

        return ds;
    }
}
