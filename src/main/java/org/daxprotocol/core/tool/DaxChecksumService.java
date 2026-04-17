package org.daxprotocol.core.tool;

import java.nio.charset.StandardCharsets;

public class DaxChecksumService {


    public  static int calculateChecksum(String input) {
        byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);

        int sum = 0;

        for (byte b : bytes) {
//                if (b != DaxpConfig.PAIR_SEPARATOR
//                && b != '\n' // TODO move to DaxpConfig create array abandoned char
//                )

//                {   // ignore pipe
            sum += b;
//                }
        }

        return sum % 256;
    }

}
