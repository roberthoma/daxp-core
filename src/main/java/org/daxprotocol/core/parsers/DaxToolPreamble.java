package org.daxprotocol.core.parsers;

import org.daxprotocol.core.config.DaxConfig;

public class DaxToolPreamble {

    public  int getEndOfPreambleIndex(String str) {
        int searchIdx = 0;

        while ((searchIdx = str.indexOf(DaxConfig.PAIR_SEPARATOR, searchIdx)) != -1) {
            int current = searchIdx + 1;
            boolean foundNine = false;
            boolean invalidPrefix = false;

            // Look ahead from the pipe
            while (current < str.length()) {
                char c = str.charAt(current);

                // 1. Skip "Noise" (Whitespaces, newlines, $, :)
                if (Character.isWhitespace(c) || c == '$' || c == ':') {
                    current++;
                    continue;
                }

                // 2. Check for invalid alphabetic prefixes (e.g., AS:9= or _9=)
                if (Character.isLetter(c) || c == '_') {
                    invalidPrefix = true;
                    break;
                }

                // 3. Check for the target '9'
                if (c == '9') {
                    foundNine = true;
                    current++;
                    continue;
                }

                // 4. Verify the '=' follows the '9' (potentially after more whitespace)
                if (foundNine && c == '=') {
                    return searchIdx + 1; // Returns index where substring should end
                }

                // If we hit another character after '9' that isn't '=', it's a different tag (e.g., 99=)
                break;
            }

            searchIdx++; // Move to next pipe if this one wasn't the start of tag 9
        }

        return -1;
    }
}

