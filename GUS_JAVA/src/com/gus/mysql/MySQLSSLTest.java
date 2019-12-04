package com.gus.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class MySQLSSLTest {

    private static final String DB_USER = "user name";
    private static final String DB_PASSWORD = "password";
    // This key store has only the prod root ca.
    private static final String KEY_STORE_FILE_PATH = "file-path-to-keystore";
    private static final String KEY_STORE_PASS = "keystore-password";
    
public static void test(String[] args) throws Exception {
    Class.forName("com.mysql.jdbc.Driver");
    
    
    System.setProperty("javax.net.ssl.trustStore", KEY_STORE_FILE_PATH);
    System.setProperty("javax.net.ssl.trustStorePassword", KEY_STORE_PASS);
    
    Properties properties = new Properties();
    properties.setProperty("sslMode", "VERIFY_IDENTITY");
    properties.put("user", DB_USER);
    properties.put("password", DB_PASSWORD);
    

    Connection connection = DriverManager.getConnection("jdbc:mysql://jagdeeps-ssl-test.cni62e2e7kwh.us-east-1.rds.amazonaws.com:3306",properties);
    Statement stmt=connection.createStatement();
    
    ResultSet rs=stmt.executeQuery("SELECT 1 from dual");

    return;
}
}
