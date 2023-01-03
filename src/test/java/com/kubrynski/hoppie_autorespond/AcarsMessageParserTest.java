package com.kubrynski.hoppie_autorespond;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AcarsMessageParserTest {

    @Test
    void parseMessage() {
        String msg = "ok {WZZ552 cpdlc {/data2/10//Y/REQUEST LOGON}} {WZZ552 cpdlc {/data2/11//Y/REQUEST LOGON}}";
        AcarsMessageParser acarsMessageParser = new AcarsMessageParser();
        List<AcarsMessage> acarsMessages = acarsMessageParser.parseMessages(msg);

        assertTrue(acarsMessages.size() == 2);
    }
}