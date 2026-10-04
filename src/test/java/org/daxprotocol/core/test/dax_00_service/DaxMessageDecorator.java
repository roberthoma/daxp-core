package org.daxprotocol.core.test.dax_00_service;


import org.daxprotocol.core.application.DaxCoreConstants;

public class DaxMessageDecorator {


    public static String decorate(String msgStr){

        return msgStr.replace("$:1=","\n$:1=")
                     .replace("$:4=","\n$:4=")
                     .replace("$:9=","\n$:9=")
          //           .replace("$:11=","$:11[name]=")
                .replace(""+DaxCoreConstants.SEPARATOR_FILE,"\n<FS>")
                .replace(""+DaxCoreConstants.SEPARATOR_GROUP,"<GS>")
                .replace(""+DaxCoreConstants.SEPARATOR_RECORD,"<RS>\n")
                .replace(""+DaxCoreConstants.SEPARATOR_UNIT,"<US>")
                .replace(""+DaxCoreConstants.END_OF_MEDIUM,"<EM>")
                .replace(""+DaxCoreConstants.DEFAULT_PAIR_SEPARATOR,"<SOH>")

                ;



    }
}
