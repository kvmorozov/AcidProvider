package ru.kmorozov.librarian.data.heroku;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sbt-morozov-kv on 13.10.2016.
 */

@RestController
public class ZooController {

    @Autowired
    private PostgreZooServer server;

    @Autowired
    private ZooKeeper zooClient;

    @RequestMapping("/")
    public String index() {
        return "ZooKeeper server status: " + getServerStatus();
    }

    private String getServerStatus() {
        if (zooClient == null)
            return "ERROR";
        else
            return zooClient.getState().name();
    }
}
