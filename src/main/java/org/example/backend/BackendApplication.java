package org.example.backend;

import org.example.backend.config.FirebaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        FirebaseConfig firebaseConfig = new FirebaseConfig();
        firebaseConfig.upload();
        firebaseConfig.download();
        SpringApplication.run(BackendApplication.class, args);
    }
}