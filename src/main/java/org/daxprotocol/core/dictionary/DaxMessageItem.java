package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.model.tag.DaxTag;

import java.util.HashSet;
import java.util.Set;

public class DaxMessageItem {

    private int   contextId;
    private final String msgType;
    private final String msgDesc;

    Set<DaxTag> reqTagMap = new HashSet<>();


    /************************
     * Related Messages
     */
    Set<String> relatedMsgTypes = new HashSet<>();
    //private final String msgRole;

    public String getMsgDesc() {
        return msgDesc;
    }


    public DaxMessageItem(String msgType, /*String msgRole,*/ String msgDesc) {
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

    public void setContextId(int contextId) {
        this.contextId = contextId;
    }

    public void addReqTag(DaxTag daxTag) {
        reqTagMap.add(daxTag);
    }

//    public void addReqTag(String daxTag) {
//        tagMap.add(daxTag);
//    }

    public Set<DaxTag> getMsgFields(){
        return reqTagMap;
    }

    public void addRelatedMsgType(String msgType) {
        relatedMsgTypes.add(msgType);
    }

    public Set<String> getRelatedMsgType() {
        return relatedMsgTypes;
    }



//    public String getMsgRole() {
//        return msgRole;
//    }
}
