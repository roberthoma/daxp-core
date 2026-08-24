package org.daxprotocol.core.unit_test.dax_00_00_service;


import org.daxprotocol.core.application.DaxCoreConstants;

public class DaxMessageDecorator {


    public static String decorate(String msgStr){

        return msgStr.replace("$:1=","\n$:1=")
                     .replace("$:4=","\n$:4=")
                     .replace("$:9=","\n$:9=")
          //           .replace("$:11=","$:11[name]=")
                .replace(DaxCoreConstants.SEPARATOR_START_OF_TEXT,'\n')
                .replace(DaxCoreConstants.SEPARATOR_END_OF_TEXT,'\n')
                .replace(DaxCoreConstants.SEPARATOR_GROUP,'>')
                .replace(DaxCoreConstants.SEPARATOR_RECORD,'\n')
                .replace(DaxCoreConstants.SEPARATOR_UNIT,'*')
                .replace(DaxCoreConstants.DEFAULT_PAIR_SEPARATOR,'|')

                ;



    }
}
