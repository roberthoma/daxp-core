package org.daxprotocol.core.data;

import java.util.HashMap;
import java.util.Map;

public class DaxMessageRegister {
     int namespaceId;
    /*****************************************************
     * Dictionary of messages type, required and respond tags
     * Key: Message type
     * */
    Map<String, DaxMessageItem> msgMap = new HashMap<>();


    // MSG namespaceId, mapperId ,
    Map<Integer, Map<Integer, DaxMessageItem> > msgMap2 = new HashMap<>();

    public DaxMessageRegister(Integer namespaceId){
        this.namespaceId = namespaceId;
    }

    public void putMsgItem(DaxMessageItem messageDicItem){
        if (msgMap.containsKey(messageDicItem.getMsgType())){
            throw new RuntimeException( "Message "+messageDicItem.getMsgType()
                    +" exists in DAXP dictionary !!!");
        }
        msgMap.put(messageDicItem.getMsgType(),messageDicItem);
    }

    public Map<String, DaxMessageItem> getMsgMap() {
        return msgMap;
    }


}
