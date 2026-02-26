package org.daxprotocol.core.parser;

import org.daxprotocol.core.config.DaxpConfig;

import java.util.regex.Pattern;

public class DaxPatternService {

    public static Pattern getPreamblePairPattern(char pairSeparator) {
        return Pattern.compile("(\\w+)" + DaxpConfig.EQUAL + "([^" + pairSeparator + "]*)");
    }

    public static Pattern getMessagePairPattern(char pairSeparator) {
        String sep = Pattern.quote(String.valueOf(pairSeparator));
        return Pattern.compile(
                "(?:([A-Za-z0-3]{0,3}):)?(\\d+)"
                        + DaxpConfig.EQUAL +
                        "([^" + sep + "]*)" +
                        sep
        );
    }
}
