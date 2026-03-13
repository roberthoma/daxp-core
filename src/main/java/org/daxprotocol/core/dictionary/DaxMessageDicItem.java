package org.daxprotocol.core.dictionary;

public class DaxMessageDicItem {


    private int contextId;

    private final String msgType;
    private final String msgDesc;

    //private final String msgRole;

    public String getMsgDesc() {
        return msgDesc;
    }


    public DaxMessageDicItem(String msgType, /*String msgRole,*/ String msgDesc) {
        this.msgType = msgType;
//        this.msgRole = msgRole;
        this.msgDesc = msgDesc;

    }


    public String getMsgType() {
        return msgType;
    }

    public int getContextId() {
        return contextId;
    }

//    public String getMsgRole() {
//        return msgRole;
//    }
}
