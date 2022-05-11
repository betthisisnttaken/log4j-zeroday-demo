package com.example.experiment02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import static org.springframework.http.ResponseEntity.ok;

@SpringBootApplication
@RestController
@Configuration
public class Experiment02Application {

    public static void main(String[] args) {
        SpringApplication.run(Experiment02Application.class, args);
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();

        filter.setIncludeQueryString(true);
        filter.setIncludePayload(false);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);

        return filter;
    }

    @GetMapping(path = "/")
    ResponseEntity<String> getMessage() {
        return ok().body("Hello Log4j exploit");
    }

}

// curl localhost:8080
// curl localhost:8080 -H "User-Agent: \${java:runtime}"