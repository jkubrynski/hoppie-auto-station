package com.kubrynski.hoppie_autorespond;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcarsMessageParserTest {

    @Test
    void parseEmptyMessage() {
        String msg = "ok";
        AcarsMessageParser acarsMessageParser = new AcarsMessageParser();
        List<AcarsMessage> acarsMessages = acarsMessageParser.parseMessages(msg);

        assertEquals(0, acarsMessages.size());
    }

    @Test
    void parseSingleMessage() {
        String msg = "ok {LOT123 cpdlc {/data2/10//Y/REQUEST LOGON}}";
        AcarsMessageParser acarsMessageParser = new AcarsMessageParser();
        List<AcarsMessage> acarsMessages = acarsMessageParser.parseMessages(msg);

        assertEquals(1, acarsMessages.size());
        AcarsMessage firstMessage = acarsMessages.get(0);
        assertEquals("LOT123", firstMessage.getStationId());
        assertEquals("10", firstMessage.getMsgId());
        assertEquals("", firstMessage.getReplyId());
        assertEquals("Y", firstMessage.getResponseType());
        assertEquals("REQUEST LOGON", firstMessage.getData());
    }

    @Test
    @Disabled("Telex messages are not parsed correctly")
    void parseSingleTelexMessage() {
        String msg = "ok {JDI65C telex {REQUEST PREDEP CLEARANCE JDI65C CL65 TO EPGD AT EPMO STAND C22 ATIS C}}";
        AcarsMessageParser acarsMessageParser = new AcarsMessageParser();
        List<AcarsMessage> acarsMessages = acarsMessageParser.parseMessages(msg);

        assertEquals(1, acarsMessages.size());
        AcarsMessage firstMessage = acarsMessages.get(0);
        assertEquals("JDI65C", firstMessage.getStationId());
        assertEquals("REQUEST PREDEP CLEARANCE JDI65C CL65 TO EPGD AT EPMO STAND C22 ATIS C", firstMessage.getData());
    }

    @Test
    void parseDualMessage() {
        String msg = "ok {LOT123 cpdlc {/data2/10//Y/REQUEST LOGON}} {WZZ552 cpdlc {/data2/16/2/N/WILCO}}";
        AcarsMessageParser acarsMessageParser = new AcarsMessageParser();
        List<AcarsMessage> acarsMessages = acarsMessageParser.parseMessages(msg);

        assertEquals(2, acarsMessages.size());
        AcarsMessage firstMessage = acarsMessages.get(0);
        assertEquals("LOT123", firstMessage.getStationId());
        assertEquals("10", firstMessage.getMsgId());
        assertEquals("", firstMessage.getReplyId());
        assertEquals("Y", firstMessage.getResponseType());
        assertEquals("REQUEST LOGON", firstMessage.getData());

        AcarsMessage secondMessage = acarsMessages.get(1);
        assertEquals("WZZ552", secondMessage.getStationId());
        assertEquals("16", secondMessage.getMsgId());
        assertEquals("2", secondMessage.getReplyId());
        assertEquals("N", secondMessage.getResponseType());
        assertEquals("WILCO", secondMessage.getData());
    }
}