package ru.kmorozov.librarian.data.heroku;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

import static ru.kmorozov.librarian.data.heroku.PostgreZooServer.getHerokuPort;

/**
 * Created by sbt-morozov-kv on 13.10.2016.
 */

@Configuration
public class ZooClientConfig {

    @Bean
    public ZooKeeper zooClient() {
        System.out.println("Init client started");
        CountDownLatch connSignal = new CountDownLatch(1);
        ZooKeeper zoo = null;
        try {
            zoo = new ZooKeeper("127.0.0.1" + ":" + getHerokuPort(), 5000, event -> {
                System.out.println("Init client status: " + event.getState().name());
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    connSignal.countDown();
                }
            });

            connSignal.await();
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("Init client failed");
            return null;
        }

        System.out.println("Init client finished");
        return zoo;
    }
}
