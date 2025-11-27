package org.daxprotocol.core.dictionary;

public class DaxMessageDicItem {

    public int getContextId() {
        return contextId;
    }

    private int contextId;

    private final String msgType;
    //private final String msgRole;
    private final String msgDesc;

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

//    public String getMsgRole() {
//        return msgRole;
//    }
}
