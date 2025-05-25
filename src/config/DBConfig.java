package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConfig {
    private static final String DB_URL = DBInfo.URL;
    private static final String USER = DBInfo.USER;
    private static final String PASS = DBInfo.PASS;

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
