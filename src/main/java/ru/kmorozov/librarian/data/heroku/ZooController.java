package ru.kmorozov.librarian.data.heroku;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CountDownLatch;

import static ru.kmorozov.librarian.data.heroku.PostgreZooServer.ZOO_PORT;

/**
 * Created by sbt-morozov-kv on 13.10.2016.
 */

@RestController
public class ZooController {

    @Autowired
    private PostgreZooServer server;

    @RequestMapping("/")
    public String index() {
        return "ZooKeeper server status: " + getServerStatus();
    }

    private String getServerStatus() {
        ZooKeeper zoo;

        try {
            CountDownLatch connSignal = new CountDownLatch(1);
            zoo = new ZooKeeper("localhost" + ":" + ZOO_PORT, 5000, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    connSignal.countDown();
                }
            });

            connSignal.await();
        } catch (Exception ex) {
            return "ERROR";
        }

        if (zoo == null)
            return "ERROR";
        else
            return zoo.getState().name();
    }
}
