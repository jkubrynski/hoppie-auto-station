package com.kubrynski.hoppie_autorespond;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class AcarsMessageResponderTest {

    DateTimeProvider dateTimeProvider = new DateTimeProvider();
    AcarsMessageResponder acarsMessageResponder = new AcarsMessageResponder(dateTimeProvider);

    @Test
    void processLogon() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/10//Y/REQUEST LOGON}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("LOGON ACCEPTED",replyObject.message);
        assertEquals("NE",replyObject.replyType);
    }

    @Test
    void processRequestDirectTo() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/2//Y/REQUEST DIRECT TO SUBIX}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("PROCEED DIRECT TO @SUBIX@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestDirectToDue() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/10//Y/REQUEST DIRECT TO GRUDA DUE TO MEDICAL}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("PROCEED DIRECT TO @GRUDA@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestClb() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/9//Y/REQUEST CLB TO FL180}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("CLIMB TO AND MAINTAIN @FL180@ REPORT LEVEL @FL180@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestClbAt() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/15//Y/REQUEST CLB TO FL250 AT SUBIX}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("AT @SUBIX@ CLIMB TO AND MAINTAIN @FL250@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestClbAtDue() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/15//Y/REQUEST CLB TO FL250 AT SUBIX DUE TO AC PERFORMANCE}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("AT @SUBIX@ CLIMB TO AND MAINTAIN @FL250@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestDes() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/9//Y/REQUEST DES TO FL180}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("DESCENT TO AND MAINTAIN @FL180@ REPORT LEVEL @FL180@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestDesAt() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/15//Y/REQUEST DES TO FL250 AT SUBIX}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("AT @SUBIX@ DESCENT TO AND MAINTAIN @FL250@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestDesAtDue() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/15//Y/REQUEST DES TO FL250 AT SUBIX DUE TO AC PERFORMANCE}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("AT @SUBIX@ DESCENT TO AND MAINTAIN @FL250@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestGroundTrack() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/5//Y/REQUEST GROUND TRACK 280}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("TURN @RIGHT@ GROUND TRACK @280@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestGroundTrackDue() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/5//Y/REQUEST GROUND TRACK 280 DUE TO WEATHER}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("TURN @RIGHT@ GROUND TRACK @280@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestHeading() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/5//Y/REQUEST HEADING 280}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("TURN @RIGHT@ HEADING @280@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestOwnSeparation() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/4//Y/REQUEST OWN SEPARATION AND VMC}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("MAINTAIN OWN SEPARATION AND VMC",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestClearance() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/4//Y/REQUEST EPWA-EPGD EPWA.EVINA.GRUDA.IRLUN.EPGD}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("CLEARED TO @EPGD@ VIA @EVINA GRUDA IRLUN@ SQUAWK @2654@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestClearanceNoVia() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/4//Y/REQUEST EPWA-EPGD}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("CLEARED TO @EPGD@ SQUAWK @2654@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestSid() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/2//Y/REQUEST EVINA4K DEPARTURE}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("CLEARED @EVINA4K@ DEPARTURE",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processRequestFL() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/4//Y/REQUEST FL350}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("CLIMB TO AND MAINTAIN @FL350@", replyObject.message);
        assertEquals("WU", replyObject.replyType);
    }

    @Test
    void processRequestFLDue() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/4//Y/REQUEST FL350 DUE TO AC PERFORMANCE}");
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        assertEquals("CLIMB TO AND MAINTAIN @FL350@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processWhenHigher() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/3//Y/WHEN CAN WE EXPECT HIGHER ALT}");
        LocalDateTime of = LocalDateTime.of(2023, 1, 1, 10, 2);
        dateTimeProvider.setClock(Clock.fixed(of.toInstant(ZoneOffset.UTC), ZoneId.of("UTC")));
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        dateTimeProvider.setClock(Clock.systemUTC());
        assertEquals("EXPECT CLIMB AT @1007 UTC@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }

    @Test
    void processWhenLower() {
        AcarsMessage acarsMessage = AcarsMessage.from("LOT123 cpdlc {/data2/28//Y/WHEN CAN WE EXPECT LOWER ALT AT PILOT DISCRETION}");
        LocalDateTime of = LocalDateTime.of(2023, 1, 1, 10, 58);
        dateTimeProvider.setClock(Clock.fixed(of.toInstant(ZoneOffset.UTC), ZoneId.of("UTC")));
        AcarsMessageResponder.ReplyObject replyObject = acarsMessageResponder.prepareReplyObject(acarsMessage);
        dateTimeProvider.setClock(Clock.systemUTC());
        assertEquals("EXPECT DESCENT AT @1103 UTC@",replyObject.message);
        assertEquals("WU",replyObject.replyType);
    }
}