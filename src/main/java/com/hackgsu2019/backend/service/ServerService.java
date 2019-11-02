package com.hackgsu2019.backend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.hackgsu2019.backend.model.AddItemByCodeModel;
import com.hackgsu2019.backend.model.Item;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ws.transport.http.HttpUrlConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServerService {
    private String url;
    private String user;
    private String password;
    private Connection connection;
    private Statement statement;

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

    public ResponseEntity addItemByCode(String requestedId) {
        if (!checkItemValidity(requestedId)) {
            System.out.println("Item is not present");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Item temp = new Item();
        System.out.println("Item is present");
        String query = "SELECT * FROM items WHERE id = \"" + requestedId + "\"";
        System.out.println("Executed: " + query);
        try {
            ResultSet resultSet = statement.executeQuery(query);
            String id = "", name = "", category = "";
            double price = 0.0;
            boolean flag = false;

            while (resultSet.next()) {

                id = resultSet.getString(1);
                name = resultSet.getString(2);
                price = Double.parseDouble(resultSet.getString(3));
                category = resultSet.getString(4);
                flag = true;
                temp = new Item(id, name, price, category);
                System.out.println("Received " + "[ " + id + ", " + name + ", " + price +
                        ", " + category + " ]");
            }
            if (flag) {
                query = "INSERT INTO cart (id, name, price) VALUES (\"" + id +
                        "\"" + ", " + "\"" + name + "\"" + ", " + price + ")";
                System.out.println(query);
                statement.execute(query);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Item>(temp, HttpStatus.OK);
    }

    public ResponseEntity viewCart() {
        String query = "SELECT * from cart";
        List<Item> list = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Item(resultSet.getString(1),
                        resultSet.getString(2),
                        Double.parseDouble(resultSet.getString(3)),
                        resultSet.getString(4)
                ));
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<List<Item>>(list, HttpStatus.OK);

    }

    public ResponseEntity testUnirest() {
//        HttpResponse<Item> response = Unirest.get("https://hackgsu-257800" +
//                ".appspot.com")
//                .header("Content-Type", "application/json")
//                .fiel("id", "4");
        HttpResponse<JsonNode> response = Unirest.post("http://httpbin.org/post")
                .header("accept", "application/json")
                .queryString("apiKey", "123")
                .field("parameter", "value")
                .field("foo", "bar")
                .asJson();
        System.out.println(response.getBody());
        System.out.println(response);


        response = Unirest.get("http://localhost:8080/shop/view-cart")
                .header("Content-Type", "application/json")
                .asJson();
        System.out.println(response.getBody());
        return new ResponseEntity<JsonNode>(HttpStatus.OK);
    }

    public ResponseEntity payment() {
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity findRelated(String itemCategory) {
        List<Item> list = new ArrayList<>();
        String query =
                "SELECT * from items WHERE category=\"" + itemCategory +
                        "\"";
        System.out.println("Execute: " + query);
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Item(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        Double.parseDouble(resultSet.getString(3)),
                        resultSet.getString(4)
                ));
            }

        } catch (Exception e) {
            System.out.println("Error in Query: " + query);
            e.printStackTrace();
        }
        return new ResponseEntity<List<Item>>(list, HttpStatus.OK);
    }

    private boolean checkItemValidity(String id) {
        if (id == null) {
            return false;
        }
        String query = "SELECT * FROM items";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String idFromDb = resultSet.getString(1);
                if (idFromDb.equals(id)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}