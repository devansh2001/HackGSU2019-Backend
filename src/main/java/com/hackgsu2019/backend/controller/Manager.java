package com.hackgsu2019.backend.controller;

import com.hackgsu2019.backend.model.AddItemByCodeModel;
import com.hackgsu2019.backend.model.Category;
import com.hackgsu2019.backend.model.Item;
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
        return serverService.test();
    }

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

    @RequestMapping(value = "/get-similar-products", method = RequestMethod.POST)
    public ResponseEntity findSimilarItems(@RequestBody AddItemByCodeModel addItemByCodeModel) {
        ServerService serverService = new ServerService();
        return serverService.findRelated(addItemByCodeModel.getId());
    }
    @RequestMapping(value = "/get-balance", method = RequestMethod.GET)
    public ResponseEntity getCurrentBalance() {
        ServerService serverService = new ServerService();
        return serverService.getCurrentBalance();
    }
    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public ResponseEntity payment() {
        ServerService serverService = new ServerService();
        return serverService.payment();
    }






    @RequestMapping(value = "/get-similar-product", method = RequestMethod.GET)
    public ResponseEntity similar(@RequestBody Item item) {
        ServerService serverService = new ServerService();
        return serverService.findRelatedByItemCategory(item.getCategory());
    }

    @RequestMapping(value = "test-unirest", method = RequestMethod.GET)
    public ResponseEntity testuni() {
        ServerService serverService = new ServerService();
        return serverService.testUnirest();
    }
}
