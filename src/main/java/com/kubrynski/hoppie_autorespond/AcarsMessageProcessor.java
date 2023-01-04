package com.kubrynski.hoppie_autorespond;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class AcarsMessageProcessor {

    private final AtomicInteger counter = new AtomicInteger();
    private final OkHttpClient httpClient = new OkHttpClient();

    private final AcarsMessageResponder acarsMessageResponder = new AcarsMessageResponder();

    private final String urlTemplate;

    AcarsMessageProcessor(String station, String secret) {
        urlTemplate = "https://www.hoppie.nl/acars/system/connect.html?from=" + station + "&logon=" + secret;
    }

    void processMessages() {
        List<AcarsMessage> acarsMessages = getAcarsMessages();
        System.out.println("Retrieved acars messages count: " + acarsMessages.size());
        acarsMessages.forEach(this::processSingleMessage);
    }

    private void processSingleMessage(AcarsMessage acarsMessage) {
        System.out.println("Processing ACARS message: " + acarsMessage);

        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);

        try {
            if (replyObject != null) {
                String url = urlTemplate + "&to=%s&type=cpdlc&packet=%s";
                String packetTemplate = "/data2/%s/%s/%s/%s";

                String packet = String.format(packetTemplate, counter.incrementAndGet(),
                        acarsMessage.getMsgId(), replyObject.replyType, replyObject.message);

                Request replyRequest = new Request.Builder()
                        .url(String.format(url, acarsMessage.getStationId(), packet))
                        .get()
                        .build();
                Response response = httpClient.newCall(replyRequest).execute();
                response.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<AcarsMessage> getAcarsMessages() {
        Request request = new Request.Builder()
                .url(urlTemplate + "&to=SERVER&type=Poll")
                .get()
                .build();

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            String message = response.body().string();
            response.close();
            return new AcarsMessageParser().parseMessages(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
