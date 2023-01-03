package com.kubrynski.hoppie_autorespond;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AcarsMessage {
    private static final Pattern MESSAGE_CODE_PATTERN = Pattern.compile("\\/data2\\/(\\d*)\\/(.*?)\\/(\\w{1,2})");
    private final String stationId;
    private final String messageType;
    private final String data;
    private final String msgId;
    private final String replyId;
    private final String responseType;

    AcarsMessage(String stationId, String messageType, String messageCode, String data) {
        this.stationId = stationId;
        this.messageType = messageType;
        Matcher matcher = MESSAGE_CODE_PATTERN.matcher(messageCode);
        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            this.msgId = matchResult.group(1);
            this.replyId = matchResult.group(2);
            this.responseType = matchResult.group(3);
        } else {
            throw new IllegalArgumentException("Not able to parse messageCode: " + messageCode);
        }
        this.data = StringUtils.substringBefore(data, " DUE TO");
    }

    public String getStationId() {
        return stationId;
    }

    public String getMsgId() {
        return msgId;
    }

    public String getReplyId() {
        return replyId;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AcarsMessage{");
        sb.append("stationId='").append(stationId).append('\'');
        sb.append(", messageType='").append(messageType).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append(", msgId='").append(msgId).append('\'');
        sb.append(", replyId='").append(replyId).append('\'');
        sb.append(", responseType='").append(responseType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
