package com.example.experiment03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.ResponseEntity.ok;

@SpringBootApplication
@RestController
@Configuration
@Slf4j
public class Experiment03Application {

    public static void main(String[] args) {
        SpringApplication.run(Experiment03Application.class, args);
    }

    @PostMapping(path = "/login")
    ResponseEntity<String> doLogin(@RequestParam("username") String username,
                                   @RequestParam("password") String password) {

        log.info("username: {} attempting to login", username);

        return ok().body("Hello " + username);
    }
}

// curl localhost:8080/login -X POST -F "username=derek" -F "password=password"
// curl localhost:8080/login -X POST -F "username=\${java:runtime}" -F "password=password"