package com.kubrynski.hoppie_autorespond;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HoppieAutoStationRunner {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);
        httpServer.start();

        AcarsMessageProcessor acarsMessageProcessor = new AcarsMessageProcessor(
                System.getenv("HOPPIE_STATION"),
                System.getenv("HOPPIE_SECRET")
        );

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
                acarsMessageProcessor::processMessages,
                0, 20, TimeUnit.SECONDS
        );
    }

}