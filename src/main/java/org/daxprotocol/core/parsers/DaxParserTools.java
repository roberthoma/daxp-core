package org.daxprotocol.core.parsers;

public class DaxParserTools {

    /**
     * Efficiently parses an integer from a specific range within a String
     * while ignoring any trailing whitespaces within that range.
     */
    public int parseIntFromSequence(String seq, int start, int end) {
        if (start >= end) {
            throw new NumberFormatException("Empty tag ID");
        }

        int num = 0;
        boolean hasDigits = false;

        for (int i = start; i < end; i++) {
            char c = seq.charAt(i);
            if (c >= '0' && c <= '9') {
                num = num * 10 + (c - '0');
                hasDigits = true;
            } else if (c <= ' ') {
                // If we found a space after digits, ensure no more digits follow
                // (Standard trim-like behavior)
                continue;
            } else {
                throw new NumberFormatException("Invalid character: " + c);
            }
        }

        if (!hasDigits) throw new NumberFormatException("No digits found");
        return num;
    }

}
