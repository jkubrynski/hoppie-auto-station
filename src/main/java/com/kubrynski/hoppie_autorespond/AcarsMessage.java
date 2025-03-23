package com.kubrynski.hoppie_autorespond;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AcarsMessage {

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("(\\S*)\\s(\\S*)\\s\\{/data2/(\\d*)/(.*?)/(\\w{1,2})/([^}]*)}");

    private final String stationId;
    private final String msgId;
    private final String replyId;
    private final String responseType;
    private final String data;

    AcarsMessage(String stationId, String msgId, String replyId, String responseType, String data) {
        this.stationId = stationId;
        this.msgId = msgId;
        this.replyId = replyId;
        this.responseType = responseType;
        this.data = StringUtils.substringBefore(StringUtils.substringBefore(data, " DUE TO"), " AT PILOT DISCRETION").replaceAll(" +", " ");
    }

    static AcarsMessage from(String rawMessage) {
        Matcher matcher = MESSAGE_PATTERN.matcher(rawMessage);

        if (matcher.find()) {
            MatchResult result = matcher.toMatchResult();
            return new AcarsMessage(result.group(1), result.group(3), result.group(4), result.group(5), result.group(6));
        } else {
            throw new IllegalArgumentException("Cannot parse message: " + rawMessage);
        }
    }

    String getStationId() {
        return stationId;
    }

    String getMsgId() {
        return msgId;
    }

    String getReplyId() {
        return replyId;
    }

    String getResponseType() {
        return responseType;
    }

    String getData() {
        return data;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AcarsMessage{");
        sb.append("stationId='").append(stationId).append('\'');
        sb.append(", msgId='").append(msgId).append('\'');
        sb.append(", replyId='").append(replyId).append('\'');
        sb.append(", responseType='").append(responseType).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
