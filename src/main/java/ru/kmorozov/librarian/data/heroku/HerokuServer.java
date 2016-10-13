package ru.kmorozov.librarian.data.heroku;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by sbt-morozov-kv on 12.10.2016.
 */

@SpringBootApplication(scanBasePackages = "ru.kmorozov.librarian.controllers," +
        "ru.kmorozov.librarian.persistence.config," +
        "ru.kmorozov.librarian.discovery.config, ru.kmorozov.librarian.data.heroku")
public class HerokuServer {

    public static void main(String args[]) {
        SpringApplication.run(HerokuServer.class, args);
    }

    @Bean
    public CommandLineRunner serverRunner(PostgreZooServer server) {
        return strings -> {
            server.run();
        };
    }
}
