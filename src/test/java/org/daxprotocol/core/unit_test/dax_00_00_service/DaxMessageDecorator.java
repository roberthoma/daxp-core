package org.daxprotocol.core.unit_test.dax_00_00_service;


public class DaxMessageDecorator {


    public static String decorate(String msgStr){

        return msgStr.replace("$:1=","\n$:1=")
                     .replace("$:4=","\n$:4=")
                     .replace("$:9=","\n$:9=")
                ;



    }
}
