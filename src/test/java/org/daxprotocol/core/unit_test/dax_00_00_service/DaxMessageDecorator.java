package org.daxprotocol.core.unit_test.dax_00_00_service;


public class DaxMessageDecorator {


    public static String decorate(String msgStr){

        return msgStr.replace("$:9=","\n$:9=")
                     .replace("$:7=","\n$:7=");

    }
}
