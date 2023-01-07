package com.kubrynski.hoppie_autorespond;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AcarsMessageResponder {

    private static final Pattern CLEARANCE_REQUEST_PATTERN = Pattern.compile("REQUEST ([A-Z]{4})-([A-Z]{4}).?(.*)");
    private static final Pattern FL_REQUEST_PATTERN = Pattern.compile("REQUEST FL([0-9]{3})(.*)");

    private final DateTimeProvider dateTimeProvider;

    AcarsMessageResponder(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    ReplyObject prepareReplyObject(AcarsMessage acarsMessage) {
        if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST LOGON")) {
            return new ReplyObject("NE", "LOGON ACCEPTED");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST DIRECT TO")) {
            String parameter = acarsMessage.getData().replace("REQUEST DIRECT TO ", "");
            return new ReplyObject("WU", "PROCEED DIRECT TO @" + parameter + "@");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST GROUND TRACK")) {
            String parameter = acarsMessage.getData().replace("REQUEST GROUND TRACK ", "");
            return new ReplyObject("WU", "TURN @RIGHT@ GROUND TRACK @" + parameter + "@");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST HEADING")) {
            String parameter = acarsMessage.getData().replace("REQUEST HEADING ", "");
            return new ReplyObject("WU", "TURN @RIGHT@ HEADING @" + parameter + "@");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST OWN SEPARATION AND VMC")) {
            return new ReplyObject("WU", "MAINTAIN OWN SEPARATION AND VMC");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST CLB TO")) {
            String parameter = acarsMessage.getData().replace("REQUEST CLB TO ", "");
            return new ReplyObject("WU", processAltRequestParameter(parameter, "CLIMB TO AND MAINTAIN"));
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST DES TO")) {
            String parameter = acarsMessage.getData().replace("REQUEST DES TO ", "");
            return new ReplyObject("WU", processAltRequestParameter(parameter, "DESCENT TO AND MAINTAIN"));
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "WHEN CAN WE EXPECT HIGHER")) {
            return new ReplyObject("WU", "EXPECT CLIMB AT @" + dateTimeProvider.currentTimePlusMinutes(5) + " UTC@");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "WHEN CAN WE EXPECT LOWER")) {
            return new ReplyObject("WU", "EXPECT DESCENT AT @" + dateTimeProvider.currentTimePlusMinutes(5) + " UTC@");
        } else if (StringUtils.startsWithIgnoreCase(acarsMessage.getData(), "REQUEST")) {
            if (StringUtils.endsWithIgnoreCase(acarsMessage.getData(), " DEPARTURE")) {
                String message = "CLEARED @" +
                        StringUtils.remove(StringUtils.remove(acarsMessage.getData(), "REQUEST "), " DEPARTURE")
                        + "@ DEPARTURE";
                return new ReplyObject("WU", message);
            }

            Matcher flRequest = FL_REQUEST_PATTERN.matcher(acarsMessage.getData());
            if (flRequest.find()) {
                MatchResult matchResult = flRequest.toMatchResult();
                String fl = matchResult.group(1);
                return new ReplyObject("WU", "CLIMB TO AND MAINTAIN @FL" + fl + "@");
            }

            Matcher clearanceRequest = CLEARANCE_REQUEST_PATTERN.matcher(acarsMessage.getData());
            if (clearanceRequest.find()) {
                MatchResult matchResult = clearanceRequest.toMatchResult();
                String from = matchResult.group(1);
                String to = matchResult.group(2);
                String via = matchResult.group(3);

                String message = "CLEARED TO @" + to + "@";
                if (StringUtils.isNoneBlank(via)) {
                    String withoutFromTo = StringUtils.remove(StringUtils.remove(via, from + "."), "." + to);
                    message += " VIA @" + StringUtils.replaceChars(withoutFromTo, '.', ' ') + "@";
                }
                message += " SQUAWK @2654@";
                return new ReplyObject("WU", message);
            }
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

    //REJOIN ROUTE BY [time]
    //EXPECT BACK ON ROUTE BY [time]

    //telex: REQUEST PREDEP CLEARANCE DLH4PM A320 TO EGKK AT EDDH STAND 04B ATIS D
    //reponse cpdlp: /data2/4//NE/FSM 2034 230105 EDDH @DLH4PM@ RCD RECEIVED @REQUEST BEING PROCESSED @STANDBY
    //response cpdlp: /data2/5//WU/CLD 2035 230105 EDDH PDC 002 @DLH4PM@ CLRD TO @EGKK@ OFF @33@ VIA @WSN4G@ CLIMB @5000 FT@ SQUAWK @2505@ REQ STARTUP ON @121.800@ @DEP FREQ AFTER 2000FT BREMEN RAD 119.525@

    //two stations AND EXPECT CPDLC TRANSFER AT [time]

    //&to=SERVER&logon=****&type=InfoReq&packet=VATATIS%20EPWA
    //ok {server info {THIS IS WARSAW INFORMATION DELTA . TIME 2030 . EXPECT UNIFORM ARRIVALS FOR ILS YANKEE APPROACH RUNWAY 33 . DEPARTURES RUNWAY 29 . TRANSITION LEVEL 8 0 . WIND 310 DEGREES 21 KNOTS . VISIBILITY 10 KILOMETERS OR MORE . CLOUDS BROKEN 1800 FEET TEMPERATURE 04 . DEWPOINT 01 . QNH 1010 . FOR ATC CLEARANCE CONTACT 121.605 . WHEN AIRBORNE CONTACT APPROACH ON 128.805 . END OF INFORMATION DELTA .}}
    //ok {server info {THIS IS EPGD INFORMATION C. TIME 2030. EXPECT TANGO ARRIVALS FOR ILS APPROACH RUNWAY 29. DEPARTURES RUNWAY 29. TRANSITION LEVEL TRANSITION LEVEL80. 35009KT 3500 -SN BR FEW002 BKN005 00/00 Q1015. END OF INFORMATION C.}}

    private static String processAltRequestParameter(String parameter, String command) {
        int i = parameter.indexOf(" AT ");
        if (i > 0) {
            String[] split = parameter.split(" AT ");
            return "AT @" + split[1] + "@ " + command + " @" + split[0] + "@";
        } else {
            return command + " @" + parameter + "@ REPORT LEVEL @" + parameter + "@";
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
