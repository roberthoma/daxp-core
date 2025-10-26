package org.daxprotocol.core.model;

import org.daxprotocol.core.attributes.DaxAttribute;
import org.daxprotocol.core.codec.DaxMsgType;
import org.daxprotocol.core.codec.DaxTag;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.body.DaxBody;

import java.util.Map;

import static org.daxprotocol.core.codec.DaxTag.*;

public class DaxMessageFactory {



    public DaxMessage createDictionaryReq() {
        DaxMessage message = new DaxMessage(DaxMsgType.DIC_REQ);

        return message;

    }


    private void putBodyBlock(DaxBody body, int fieldId, Map<Integer, DaxAttribute<?>> map){
        body.nextBlock();
        body.putPair(FIELD_ID,String.valueOf( fieldId));
        map.forEach((i, a) -> body.putPair(i,a.getValue().toString()));


    }

    public DaxMessage createDictionaryMsg(DaxDictionary dictionary) {
        DaxMessage message = new DaxMessage(DaxMsgType.DATA_DIC);
        dictionary.getAttrMap().forEach((fieldId, atrMap) ->
                        putBodyBlock(message.getBody(),fieldId,  atrMap)
                );
        return message;

    }


}
