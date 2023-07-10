package com.kubrynski.hoppie_autorespond;

import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class HoppieAutoStationRunner {

    private static final AtomicLong TIME = new AtomicLong(System.currentTimeMillis());

    public static void main(String[] args) throws IOException {
        System.out.println("Running Hoppie Auto Station...");
        int port = Integer.parseInt(StringUtils.defaultIfBlank(System.getenv("PORT"), "8080"));
        System.out.println("Starting health check responder on port " + port + "...");
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        httpServer.createContext("/", exchange -> {
            String response;
            if (System.currentTimeMillis() - TIME.get() > TimeUnit.SECONDS.toMillis(45)) {
                response = "NOK";
                exchange.sendResponseHeaders(500, response.length());
                System.out.println("Responded to health check with NOK");
            } else {
                response = "OK";
                exchange.sendResponseHeaders(200, response.length());
                System.out.println("Responded to health check with OK");
            }
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();
        });
        httpServer.start();

        AcarsMessageProcessor acarsMessageProcessor = new AcarsMessageProcessor(
                System.getenv("HOPPIE_STATION"),
                System.getenv("HOPPIE_SECRET")
        );

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
                () -> {
                    acarsMessageProcessor.processMessages();
                    TIME.set(System.currentTimeMillis());
                },
                0, 20, TimeUnit.SECONDS
        );
    }

}