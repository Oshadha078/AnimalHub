package com.domesticanimalhub.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    // JDBC 
    private static final String JDBC_URL  = "jdbc:mysql://127.0.0.1:3306/Animal_V2?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";  
    private static final String JDBC_PASS = "root";   
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a new DB connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }
}
