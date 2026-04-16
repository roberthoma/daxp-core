package org.daxprotocol.core.decorator;

import org.daxprotocol.core.application.DaxCoreConstants;

public class DaxMessageNormalizer {


    public static String normalize(String msgStr){

        return msgStr.replace('|', DaxCoreConstants.PAIR_SEPARATOR);

    }
}
