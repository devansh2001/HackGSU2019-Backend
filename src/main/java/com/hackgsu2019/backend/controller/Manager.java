package com.hackgsu2019.backend.controller;

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
}
