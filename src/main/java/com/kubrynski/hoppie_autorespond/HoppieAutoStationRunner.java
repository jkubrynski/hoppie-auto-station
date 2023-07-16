package com.kubrynski.hoppie_autorespond;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class HoppieAutoStationRunner {

    private static final AtomicLong TIME = new AtomicLong(System.currentTimeMillis());

    public static void main(String[] args) {
        System.out.println("Running Hoppie Auto Station...");

        AcarsMessageProcessor acarsMessageProcessor = new AcarsMessageProcessor(
                System.getenv("HOPPIE_STATION"),
                System.getenv("HOPPIE_SECRET")
        );

        TIME.set(System.currentTimeMillis());

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(
                () -> {
                    if (System.currentTimeMillis() - TIME.get() > TimeUnit.SECONDS.toMillis(45)) {
                        System.out.println("Exiting due to timeout");
                        System.exit(1);
                    }
                    try {
                        acarsMessageProcessor.processMessages();
                    } catch (Throwable e) {
                        System.out.println("Encountered problem " + e.getMessage());
                        System.exit(1);
                    }
                    TIME.set(System.currentTimeMillis());
                },
                0, 20, TimeUnit.SECONDS
        );
    }

}