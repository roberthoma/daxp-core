package org.daxprotocol.core.dictionary;

import java.util.HashMap;
import java.util.Map;

public class DaxMessageDic {


    /*****************************************************
     * Dictionary of messages type, roles
     * Key: Message type
     * */
    Map<String, DaxMessageDicItem> msgMap = new HashMap<>();



    public void putMsgItem(DaxMessageDicItem messageDicItem){
        if (msgMap.containsKey(messageDicItem.getMsgType())){
            throw new RuntimeException( "Message "+messageDicItem.getMsgType()
                    +" exists in DAXP dictionary !!!");
        }
        msgMap.put(messageDicItem.getMsgType(),messageDicItem);
    }

    public Map<String, DaxMessageDicItem> getMsgMap() {
        return msgMap;
    }

}
