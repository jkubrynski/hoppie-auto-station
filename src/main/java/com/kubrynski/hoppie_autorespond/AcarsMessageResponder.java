package com.kubrynski.hoppie_autorespond;

import org.apache.commons.lang3.StringUtils;

class AcarsMessageResponder {

    ReplyObject prepareReplyObject(AcarsMessage acarsMessage) {
        if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST LOGON")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming logon");
            return new ReplyObject("NE", "LOGON ACCEPTED");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST DIRECT TO")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming DIRECT TO");
            String parameter = acarsMessage.getData().replace("REQUEST DIRECT TO ", "");
            return new ReplyObject("WU", "PROCEED DIRECT TO @" + parameter + "@");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST GROUND TRACK")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming GROUND TRACK");
            String parameter = acarsMessage.getData().replace("REQUEST GROUND TRACK ", "");
            return new ReplyObject("WU", "TURN @RIGHT@ GROUND TRACK @" + parameter + "@");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST HEADING")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming HEADING");
            String parameter = acarsMessage.getData().replace("REQUEST HEADING ", "");
            return new ReplyObject("WU", "TURN @RIGHT@ HEADING @" + parameter + "@");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST OWN SEPARATION AND VMC")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming OWN SEPARATION");
            return new ReplyObject("WU", "MAINTAIN OWN SEPARATION AND VMC");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST CLB TO")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming REQUEST CLB TO");
            String parameter = acarsMessage.getData().replace("REQUEST CLB TO ", "");
            return new ReplyObject("WU", processAltRequestParameter(parameter, "CLIMB TO AND MAINTAIN"));
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST DES TO")) {
            System.out.println(acarsMessage.getStationId() + " => Confirming REQUEST DES TO");
            String parameter = acarsMessage.getData().replace("REQUEST DES TO ", "");
            return new ReplyObject("WU", processAltRequestParameter(parameter, "DESCENT TO AND MAINTAIN"));
        } else if ("LOGOFF".equals(acarsMessage.getData())) {
            System.out.println(acarsMessage.getStationId() + " => Logoff");
        } else {
            System.out.println("Received not serviceable message: " + acarsMessage);
        }
        return null;
    }

    //WHEN CAN WE EXPECT BACK ON ROUTE
    //WHEN CAN WE EXPECT CRUISE CLIMB TO [altitude]
    //WHEN CAN WE EXPECT HIGHER ALTITUDE
    //WHEN CAN WE EXPECT LOWER ALTITUDE

    //WHEN CAN WE EXPECT DESCENT TO [altitude]
    //WHEN CAN WE EXPECT CLIMB TO [altitude]

    //EXPECT DESCENT AT [time]
    //EXPECT CLIMB AT [time]
    //EXPECT CRUISE CLIMB AT [time]

    // answers
    //PROCEED BACK ON ROUTE

    //CLEARED [route][procedure] - maybe for all REQUEST?

    //REJOIN ROUTE BY [time]
    //EXPECT BACK ON ROUTE BY [time]

    //CLIMB TO AND MAINTAIN FL390
    //REPORT LEVEL FL390

    //two stations AND EXPECT CPDLC TRANSFER AT [time]

    private static String processAltRequestParameter(String parameter, String command) {
        int i = parameter.indexOf(" AT ");
        if (i > 0) {
            String[] split = parameter.split(" AT ");
            return "AT @" + split[1] + "@ " + command + " @" + split[0] + "@";
        } else {
            return command + " @" + parameter + "@";
        }
    }

    static class ReplyObject {
        String replyType;
        String message;

        ReplyObject(String replyType, String message) {
            this.replyType = replyType;
            this.message = message;
        }
    }
}
