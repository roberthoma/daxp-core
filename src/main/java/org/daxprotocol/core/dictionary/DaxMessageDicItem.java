package org.daxprotocol.core.dictionary;

public class DaxMessageDicItem {

    private final String msgType;
    private final String msgRole;

    public DaxMessageDicItem(String msgType, String msgRole) {
        this.msgType = msgType;
        this.msgRole = msgRole;
    }


    public String getMsgType() {
        return msgType;
    }

    public String getMsgRole() {
        return msgRole;
    }
}
