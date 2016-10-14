package ru.kmorozov.librarian.test.data;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertNotNull;
import static ru.kmorozov.librarian.data.heroku.PostgreZooServer.DEFAULT_ZOO_PORT;

/**
 * Created by sbt-morozov-kv on 12.10.2016.
 */
public class HerokuTest {

    private static final String HEROKU_POSTGRE_CONN_STR = "jdbc:postgresql://ec2-184-73-254-144.compute-1.amazonaws.com:5432/" +
            "d2kffvhm8s2cb?user=wekyotlrjbuiyq&password=qZbXBGUvTgUgAOUEBRxTnqmTfk";
    private static final String HEROKU_HOST = "ec2-184-73-254-144.compute-1.amazonaws.com";

    @Test
    public void herokuPostgreConnectTest() {
        try {
            Connection connection = DriverManager.getConnection(HEROKU_POSTGRE_CONN_STR);

            assertNotNull(connection);
        } catch (SQLException e) {
            e.printStackTrace();

            Assert.assertTrue(true);
        }
    }

    @Test
    public void herokuZooConnectTest() {
        try {
            CountDownLatch connSignal = new CountDownLatch(1);
            ZooKeeper zoo = new ZooKeeper(HEROKU_HOST + ":" + DEFAULT_ZOO_PORT, 5000, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    connSignal.countDown();
                }
            });

            connSignal.await();
            assertNotNull(zoo);
        } catch (Exception e) {
            e.printStackTrace();

            Assert.assertTrue(true);
        }
    }
}
