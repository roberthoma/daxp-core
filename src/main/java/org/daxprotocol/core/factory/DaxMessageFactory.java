package org.daxprotocol.core.factory;

import org.daxprotocol.core.annotation.DaxpTag;
import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.head.DaxMsgType;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.trailer.DaxTrailer;

import java.lang.reflect.Field;
import java.util.Map;

import static org.daxprotocol.core.codec.DaxTag.*;

public class DaxMessageFactory {



    public DaxMessage createDictionaryReq() {
        return new DaxMessage(DaxMsgType.DIC_REQ);
    }


    private void putBodyBlock(DaxBody body, int fieldId, Map<Integer, DaxPair<?>> map){
        body.nextBlock();
        body.putPair(FIELD_ID,String.valueOf( fieldId));
        map.forEach((i, pair) -> body.putPair(pair));

    }

    public DaxMessage createDictionaryMsg(DaxDictionary dictionary) {
        DaxMessage message = new DaxMessage(DaxMsgType.DATA_DIC);
        dictionary.getAttributMap().forEach((fieldId, atrMap) ->
                        putBodyBlock(message.getBody(),fieldId,  atrMap)
                );
        return message;

    }


    public DaxMessage toDaxMessage(String messageType, Object daxDataEntry ){
        DaxPreamble preamble = new DaxPreamble();
        DaxHead head = new DaxHead(messageType);
        DaxBody body = new DaxBody();
        body.nextBlock();
        DaxTrailer trailer = new DaxTrailer();
        try {
            for (Field field : daxDataEntry.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(DaxpTag.class)) {
                    DaxpTag daxp = field.getAnnotation(DaxpTag.class);
                    field.setAccessible(true);
                    if (field.get(daxDataEntry) != null) { //TODO For String check is empty
                        body.putPair(new DaxPair<>(daxp.tag(), field.get(daxDataEntry)));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new DaxMessage(preamble,head,body,trailer);

    }

}
