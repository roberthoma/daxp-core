package org.daxprotocol.core.parsers;

import org.daxprotocol.core.config.DaxpConfig;

import java.util.regex.Pattern;

public class DaxPatternFactory {

    public static Pattern compilePreamblePairPattern(char pairSeparator) {
        return Pattern.compile("(\\w+)" + DaxpConfig.EQUAL + "([^" + pairSeparator + "]*)");
    }

    public static Pattern compileMessagePairPattern(char pairSeparator) {
        String sep = Pattern.quote(String.valueOf(pairSeparator));
        return Pattern.compile(
                "(?:([A-Za-z0-3]{0,3}):)?(\\d+)"
                        + DaxpConfig.EQUAL +
                        "([^" + sep + "]*)" +
                        sep
        );
    }

    public static Pattern compileContextTagPattern(DaxpConfig config) {
        return Pattern.compile("^(?:([A-Za-z]+)"+ config.getContextTafSeparator()+")?([0-9]+)$");
    }
}
