package org.daxprotocol.core.context;

import java.util.HashMap;
import java.util.Map;

public class DaxMessageDictionary {
     int contextId;
    /*****************************************************
     * Dictionary of messages type, required and respond tags
     * Key: Message type
     * */
    Map<String, DaxMessageItem> msgMap = new HashMap<>();


    // MSG ContextId, mapperId ,
    Map<Integer, Map<Integer, DaxMessageItem> > msgMap2 = new HashMap<>();

    public DaxMessageDictionary(Integer contextId){
        this.contextId = contextId;
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
