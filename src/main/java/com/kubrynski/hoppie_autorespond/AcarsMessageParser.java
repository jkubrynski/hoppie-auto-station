package com.kubrynski.hoppie_autorespond;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class AcarsMessageParser {

    private static final Pattern MESSAGE_REGEX = Pattern.compile("\\{(.*?})}");

    List<AcarsMessage> parseMessages(String message) {
        Matcher matcher = MESSAGE_REGEX.matcher(message);

        return matcher.results()
                .map(result -> AcarsMessage.from(result.group(1)))
                .collect(Collectors.toList());
    }
}
