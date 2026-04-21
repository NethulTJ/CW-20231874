package com.mycompany.cw;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class CW {
    private static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {
        ResourceConfig config = ResourceConfig.forApplication(new RestApplication());
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
    }

    public static void main(String[] args) throws InterruptedException {
        HttpServer server = startServer();
        CountDownLatch keepAlive = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down Smart Campus API...");
            server.shutdownNow();
            keepAlive.countDown();
        }));

        System.out.println("Smart Campus API running at " + BASE_URI + "api/v1");
        System.out.println("Stop the process to shut the server down.");
        keepAlive.await();
    }
}
