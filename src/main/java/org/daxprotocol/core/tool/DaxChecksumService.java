package org.daxprotocol.core.tool;

import java.nio.charset.StandardCharsets;

public class DaxChecksumService {

    public static int calculateSum(String input){
        byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);
        int sum = 0;

        for (byte b : bytes) {
            sum += b;
        }
        return sum;

    }


    public static int calculateModulo(int sum){
        return sum % 256;
    }

    public  static int calculateChecksum(String input) {
        return calculateModulo(calculateSum(input));
    }

}
