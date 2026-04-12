package org.daxprotocol.core.decorator;

import org.daxprotocol.core.config.DaxConfig;

public class DaxMessageDecorator {


    public static String decorate(String msgStr){

        return msgStr.replace(DaxConfig.PAIR_SEPARATOR, '|')
                     .replace("|9=","|\n9=")
                     .replace("|$9=","|\n$9=")
                     .replace("|7","|\n7");

    }
}
