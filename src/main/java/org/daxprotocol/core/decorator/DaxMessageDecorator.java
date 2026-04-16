package org.daxprotocol.core.decorator;

import org.daxprotocol.core.application.DaxCoreConstants;

public class DaxMessageDecorator {


    public static String decorate(String msgStr){

        return msgStr.replace(DaxCoreConstants.PAIR_SEPARATOR, '|')
                     .replace("|9=","|\n9=")
                     .replace("|$:9=","|\n$:9=")
                     .replace("|$:7=","|\n$:7=")
                     .replace("|7","|\n7");

    }
}
