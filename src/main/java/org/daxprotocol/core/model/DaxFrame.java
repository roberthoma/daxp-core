package org.daxprotocol.core.model;

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.model.preamble.DaxPreamble;

import java.util.ArrayList;
import java.util.List;

public class DaxFrame {
    DaxPreamble preamble;
    List<DaxMessage> messageList = new ArrayList<>();

//    DaxMessageCodec codec;
//    public String toDaxString(){
//
//        return  codec.encodeAll(messageList);
//
//    }

    public void setMessageList(List<DaxMessage> messageList) {
        this.messageList = messageList;
    }

    public void addAllMessages(List<DaxMessage> messageList){
        this.messageList.addAll(messageList);
    }

    public void addMessage(DaxMessage message) {
        this.messageList.add(message);
    }

    public DaxMessage getFirstMessage() {
        return messageList.get(0);
    }

    public void setPreamble(DaxPreamble preamble) {
        this.preamble = preamble;
    }

    public DaxPreamble getPreamble() {
        return preamble;
    }
    public List<DaxMessage> getAllMessage(){
       return messageList;
    }
}
