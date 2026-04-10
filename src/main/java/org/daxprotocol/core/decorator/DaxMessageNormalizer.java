package org.daxprotocol.core.decorator;

import org.daxprotocol.core.config.DaxConfig;

public class DaxMessageNormalizer {


    public static String normalize(String msgStr){

        return msgStr.replace('|', DaxConfig.PAIR_SEPARATOR);

    }
}
