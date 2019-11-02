package com.hackgsu2019.backend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ws.transport.http.HttpUrlConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class ServerService {
    String url;
    String user;
    String password;
    Connection connection;
    Statement statement;

    public ServerService() {
        System.out.println("Hello");
        this.url = "jdbc:mysql://remotemysql.com:3306/t4gILLj6mz?";
        this.user = "t4gILLj6mz";
        this.password = "2HJD46jmUv";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(this.url, this.user
                    , this.password);
            this.statement = connection.createStatement();
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            System.out.println("error");
        }
    }

    public ResponseEntity test() throws IOException {
        System.out.println("In test");
        String query =
                "SELECT * FROM items";
        try {
            ResultSet rs = statement.executeQuery(query);
            System.out.println(rs);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
//            while (rs.next()) {
//                System.out.println(rs.getString(0));
//                System.out.println(rs.getString(1));
//            }
        } catch (SQLException exception) {
            System.out.println("Error");
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}