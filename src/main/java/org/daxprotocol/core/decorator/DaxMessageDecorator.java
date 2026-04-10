package org.daxprotocol.core.decorator;

import org.daxprotocol.core.config.DaxConfig;

public class DaxMessageDecorator {


    public static String decorate(String msgStr){

        return msgStr.replace(DaxConfig.PAIR_SEPARATOR, '|').replace("|7","|\n7");

    }
}
