package com.kubrynski.hoppie_autorespond;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class AcarsMessageParser {

    private static final Pattern MESSAGE_REGEX = Pattern.compile("\\{(\\S*)\\s(\\S*)\\s\\{(\\/\\S*\\/|TELEX\\s)([^\\}]*)\\}\\}");

    List<AcarsMessage> parseMessages(String message) {
        Matcher matcher = MESSAGE_REGEX.matcher(message);

        return matcher.results()
                .map(result -> new AcarsMessage(result.group(1), result.group(2), result.group(3), result.group(4)))
                .collect(Collectors.toList());
    }
}
