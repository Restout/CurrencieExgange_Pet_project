package org.example;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class DataSource {
    public static SQLiteDataSource getDataSource(){
        SQLiteDataSource ds=new SQLiteDataSource();
        ds.setUrl("jdbc:sqlite:D:\\SQLite\\sqlite-tools-win32-x86-3420000\\SQLiteDataBasesCurExch.db");
        return ds;
    }
}
