package org.daxprotocol.core.parsers;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.daxprotocol.core.application.DaxCoreConstants.*;

public class DaxBulkCollectionParser {

    public static Set<String> parseBulkToSet(byte[] payload) {
        if (payload == null || payload.length == 0) {
            return Collections.emptySet();
        }

        Set<String> strList = new LinkedHashSet<>();
        int recordStart = -1;

        for (int i = 0; i < payload.length; i++) {
            byte b = payload[i];

            if (b == (byte) END_OF_MEDIUM) {
                if (recordStart != -1) {
                    addRecord(payload, recordStart, i, strList);
                }
                return strList; // Stop reading on <EM>
            }

            if (b == (byte) SEPARATOR_FILE) {
                continue;
            }

            if (b == (byte) SEPARATOR_RECORD) {
                if (recordStart != -1) {
                    addRecord(payload, recordStart, i, strList);
                    recordStart = -1;
                }
            } else if (recordStart == -1 && !Character.isWhitespace((char) b)) {
                recordStart = i; // Mark start of non-whitespace record content
            }
        }

        if (recordStart != -1) {
            addRecord(payload, recordStart, payload.length, strList);
        }

        return strList;
    }

    private static void addRecord(byte[] data, int start, int end, Set<String> targetSet) {
        String record = new String(data, start, end - start, StandardCharsets.UTF_8).trim();
        if (!record.isEmpty()) {
            targetSet.add(record);
        }
    }
}
