package com.hackgsu2019.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.hackgsu2019.backend.service.ServerService;
import com.google.firebase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;

@Configuration
public class Config {
//    @Bean
//    public FirebaseApp firebaseAuth() throws Exception {
//        File f = new File("/Users/devanshjatinponda/HackGSU2019/src/main/java/com/hackgsu2019/backend/config/myfile.txt");
//        System.out.println("Checking if my file exists");
//        System.out.println(f.exists());
//
//        FileInputStream serviceAccount;
//        FirebaseOptions options;
//
//            serviceAccount = new FileInputStream("/Users/devanshjatinponda/HackGSU2019/src/main/java/com/hackgsu2019/backend/config/hackgsu.json");
//            options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://hackgsu-257800.firebaseio.com")
//                    .build();
//
//        return FirebaseApp.initializeApp(options);
//    }

    @Bean
    public ServerService serverService() {
        return new ServerService();
    }
}
