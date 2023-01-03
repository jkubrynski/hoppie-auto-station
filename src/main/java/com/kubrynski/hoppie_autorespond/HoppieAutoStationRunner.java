package com.kubrynski.hoppie_autorespond;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HoppieAutoStationRunner {

    public static void main(String[] args) {
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