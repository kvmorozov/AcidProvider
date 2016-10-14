package ru.kmorozov.librarian.data.heroku;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by km on 12.10.2016.
 */

@Component
public class PostgreZooServer {

    public static final int ZOO_PORT = 34383;

    private final ZooKeeperServerMain server;
    private final ServerConfig serverConfig;

    PostgreZooServer() {
        Properties properties = new Properties();
        properties.put("dataDir", System.getProperty("java.io.tmpdir"));
        properties.put("dataLogDir", System.getProperty("java.io.tmpdir"));
        properties.put("clientPort", ZOO_PORT);
        properties.put("tickTime", 2000);
        properties.put("maxClientCnxns", 20);

        QuorumPeerConfig config = new QuorumPeerConfig();
        try {
            config.parseProperties(properties);
        } catch (IOException | QuorumPeerConfig.ConfigException e) {
            e.printStackTrace();
        }

        server = new ZooKeeperServerMain();
        serverConfig = new ServerConfig();
        serverConfig.readFrom(config);
    }

    void run() {
        new Thread() {
            @Override
            public void run() {
                try {
                    server.runFromConfig(serverConfig);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public ZooKeeperServerMain getServer() {
        return server;
    }
}
