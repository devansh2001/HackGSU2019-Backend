package com.hackgsu2019.backend.controller;

import com.hackgsu2019.backend.model.AddItemByCodeModel;
import com.hackgsu2019.backend.service.ServerService;
import lombok.AllArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

@ComponentScan
@RestController
@EnableAutoConfiguration
@AllArgsConstructor
@RequestMapping("/shop")
public class Manager {

    @GetMapping("/test")
    public ResponseEntity refresh() throws IOException {
        ServerService serverService = new ServerService();
//        final MediaType JSON = MediaType.get("application/json; charset=utf-8");

//        RequestBody body = RequestBody.create(JSON, json);
        return serverService.test();
    }

//    @PostMapping("/add-item-by-code")
    @RequestMapping(value = "/add-item-by-code", method = RequestMethod.POST)
    public ResponseEntity addItemByCode(@RequestBody AddItemByCodeModel addItemByCodeModel) {
        ServerService serverService = new ServerService();
        return serverService.addItemByCode(addItemByCodeModel.getId());
    }

    @RequestMapping(value = "/view-cart", method = RequestMethod.GET)
    public ResponseEntity viewCart() {
        ServerService serverService = new ServerService();
        return serverService.viewCart();
    }
}
