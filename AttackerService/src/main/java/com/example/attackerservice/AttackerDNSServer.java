package com.example.attackerservice;

import lombok.extern.slf4j.Slf4j;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Slf4j
public class AttackerDNSServer {
    private Thread thread = null;
    private volatile boolean running = false;
    private static final int UDP_SIZE = 512;
    private final int port;
    private int requestCount = 0;

    public AttackerDNSServer() {
        this(53);
    }

    public AttackerDNSServer(int port) {
        this.port = port;
    }

    @PostConstruct
    public void start() {
        log.info("Starting Attacker DNS server on port: {}", port);
        running = true;
        thread = new Thread(() -> {
            try {
                serve();
            } catch (IOException ex) {
                stop();
                throw new RuntimeException(ex);
            }
        });
        thread.start();
    }

    @PreDestroy
    public void stop() {
        log.info("Stopping Attacker DNS server on port: {}", port);
        running = false;
        thread.interrupt();
        thread = null;
    }

    private void serve() throws IOException {
        DatagramSocket socket = new DatagramSocket(port);
        while (running) {
            process(socket);
        }
    }

    private void process(DatagramSocket socket) throws IOException {
        byte[] in = new byte[UDP_SIZE];

        // Read the request
        DatagramPacket indp = new DatagramPacket(in, UDP_SIZE);
        socket.receive(indp);
        // Build the response
        Message request = new Message(in);
        Message response = new Message(request.getHeader().getID());
        Record r = request.getQuestion();
        log.info("request of type: {}, for name: {}", r.getType(), r.getName());
        // setup header
        Header h = new Header();
        h.setID(request.getHeader().getID());
        h.setFlag(Flags.QR);
        response.setHeader(h);
        response.addRecord(request.getQuestion(), Section.QUESTION);
        // Add answers as needed
        if(r.getType() == Type.TXT) {
            response.addRecord(new TXTRecord(r.getName(), DClass.IN,  865400, "you've been exploited"), Section.ANSWER);
        }
        else {
            response.addRecord(Record.fromString(r.getName(), Type.A, DClass.IN, 86400, "1.2.3.4", Name.root), Section.ANSWER);
        }

        byte[] resp = response.toWire();
        socket.send(new DatagramPacket(resp, resp.length, indp.getAddress(), indp.getPort()));
    }
}
