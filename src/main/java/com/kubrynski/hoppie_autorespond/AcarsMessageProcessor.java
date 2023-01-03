package com.kubrynski.hoppie_autorespond;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AcarsMessageProcessor {

    private final AtomicInteger counter = new AtomicInteger();
    private final OkHttpClient httpClient = new OkHttpClient();

    private final String urlTemplate;

    public AcarsMessageProcessor(String station, String secret) {
        urlTemplate = "https://www.hoppie.nl/acars/system/connect.html?from=" + station + "&logon=" + secret;
    }

    void processMessages() {
        List<AcarsMessage> acarsMessages = getAcarsMessages();
        System.out.println("Retrieved acars messages count: " + acarsMessages.size());
        acarsMessages.forEach(this::processSingleMessage);
    }

    private void processSingleMessage(AcarsMessage acarsMessage) {
        System.out.println("Processing ACARS message: " + acarsMessage);

        String url = urlTemplate + "&to=%s&type=cpdlc&packet=%s";

        String packetTemplate = null;
        String msg = null;

        if ("REQUEST LOGON".equals(acarsMessage.getData())) {
            System.out.println(acarsMessage.getStationId() + " => Confirming logon");
            packetTemplate = "/data2/%s/%s/NE/%s";
            msg = "LOGON%20ACCEPTED";


        } else if (acarsMessage.getData().startsWith("REQUEST DIRECT TO")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming DIRECT TO");

            String waypointId = acarsMessage.getData().replace("REQUEST DIRECT TO ", "");

            packetTemplate = "/data2/%s/%s/WU/%s";
            msg = "PROCEED DIRECT TO @" + waypointId;
        } else if (acarsMessage.getData().startsWith("REQUEST CLB TO")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming REQUEST CLB TO");

            String parameter = acarsMessage.getData().replace("REQUEST CLB TO ", "");

            msg = processAltRequestParameter(parameter, "CLIMB TO AND MAINTAIN");
            packetTemplate = "/data2/%s/%s/WU/%s";
        } else if (acarsMessage.getData().startsWith("REQUEST DES TO")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming REQUEST DES TO");

            String parameter = acarsMessage.getData().replace("REQUEST DES TO ", "");

            msg = processAltRequestParameter(parameter, "DESCENT TO AND MAINTAIN");
            packetTemplate = "/data2/%s/%s/WU/%s";
        } else if ("WILCO".equals(acarsMessage.getData())) {
            System.out.println(acarsMessage.getStationId() + " => WILCO for message " + acarsMessage.getReplyId());
        } else if ("UNABLE".equals(acarsMessage.getData())) {
            System.out.println(acarsMessage.getStationId() + " => UNABLE for message " + acarsMessage.getReplyId());
        } else if ("LOGOFF".equals(acarsMessage.getData())) {
            System.out.println(acarsMessage.getStationId() + " => Logoff");
        } else {
            System.out.println("Received not serviceable message: " + acarsMessage);
        }

        try {
            if (msg != null) {
                String packet = String.format(packetTemplate, counter.incrementAndGet(), acarsMessage.getMsgId(), msg);
                Request request1 = new Request.Builder()
                        .url(String.format(url, acarsMessage.getStationId(), packet))
                        .get()
                        .build();
                Response response = httpClient.newCall(request1).execute();
                response.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String processAltRequestParameter(String parameter, String command) {
        int i = parameter.indexOf(" AT ");
        if (i > 0) {
            String[] split = parameter.split(" AT ");
            return "AT @" + split[1] + "@ " + command + " @" + split[0];
        } else {
            return command + " @" + parameter;
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
