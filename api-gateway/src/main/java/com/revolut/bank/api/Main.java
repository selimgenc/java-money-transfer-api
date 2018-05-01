package com.revolut.bank.api;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class Main {
    private static final URI BASE_URI = URI.create("http://localhost:8090/api/");

    public static void main(String[] consoleArgs) throws Exception {
        ResourceConfig resourceConfig = new JaxrsApp();
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig, false);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
        server.start();
    }

}
