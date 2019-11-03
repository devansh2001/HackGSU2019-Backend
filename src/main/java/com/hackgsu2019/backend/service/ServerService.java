package com.hackgsu2019.backend.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.*;
import com.hackgsu2019.backend.model.AddItemByCodeModel;
import com.hackgsu2019.backend.model.Item;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
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
    private String ncrConnectionKey;

    private String sourceAccountId = "rf5ao6Qclwsth9OfOvUb-EeV1m2BfmTzUEALGLQ3ehU";
    private String toAccountId = "W4vrnyCIYqtzYybyi1dChNBtVD7kWbvrTEljsZq5z6Y";


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

    private String getAccessToken() {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&scopes=accounts%3Aread%2Ctransactions%3Aread");
        Request request = new Request.Builder()
                .url("http://ncrdev-dev.apigee.net/digitalbanking/oauth2/v1/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic alI3RWg3dUF5cFQ0dEpMb0xVMmRBTVlHQ1l5ejZsVjg6T3FRZXQ0OE5YWDdTQXB4SA==")
                .addHeader("transactionId", "b5571b8a-6ffe-4872-a13f-f60a2f1907cc")
                .addHeader("institutionId", "00516")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "PostmanRuntime/7.19.0")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "e4fbc0cb-95a7-42c3-806f-d9beba14ae92,111f6a7c-0f0f-4197-9b4a-e321e9b81686")
                .addHeader("Host", "ncrdev-dev.apigee.net")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Length", "74")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response.toString());
            JSONObject obj = new JSONObject(response.body().string());
            return obj.get("access_token").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseEntity getCurrentBalance() {
        double currentBalance = 0.0;
        String query = "SELECT * FROM balance";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                currentBalance = Double.parseDouble(resultSet.getString(1));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Double>(currentBalance, HttpStatus.OK);
    }

    public ResponseEntity payment() {
        this.ncrConnectionKey = getAccessToken();
        while (this.ncrConnectionKey == null) {
            this.ncrConnectionKey = getAccessToken();
        }
        double currentBalance = 0.0;
        double bill = 0.0;
        double remainingBalance = 0.0;
        String query = "SELECT * FROM balance";
        String query2 = "SELECT SUM(price) from cart";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                currentBalance = Double.parseDouble(resultSet.getString(1));
            }
            resultSet = statement.executeQuery(query2);
            while(resultSet.next()) {
                bill = Double.parseDouble(resultSet.getString(1));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        remainingBalance = currentBalance - bill;
        query =
                "INSERT INTO balance (remainingMoney) VALUES (" + remainingBalance + ")";
        try {
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Double>(remainingBalance, HttpStatus.OK);

    }

    public ResponseEntity findRelated(String id) {
        List<Item> list = new ArrayList<>();
        String query = "SELECT * from items WHERE id=\"" + id + "\"";
        System.out.println("Execute: " + query);
        String category = "";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (resultSet.getString(1).equals(id)) {
                    category = resultSet.getString(4);
                }
            }
        } catch (Exception e) {
            System.out.println("Error in Query: " + query);
            e.printStackTrace();
        }
        query = "SELECT * FROM items WHERE category=\"" + category + "\"";
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
            System.out.println("Error in query: " + query);
            e.printStackTrace();
        }
        return new ResponseEntity<List<Item>>(list, HttpStatus.OK);
    }

    public ResponseEntity findRelatedByItemCategory(String category) {
        String query = "SELECT * FROM items WHERE category=\"" + category +
                "\"";
        System.out.println(query);
        return new ResponseEntity(HttpStatus.OK);
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