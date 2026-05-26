package org.daxprotocol.core.factory;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.context.DaxContext;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.dictionary.DaxBaseDictionary;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxMessageItem;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.tag.DaxTagDestiny;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxDictionaryMessageFactory {

    DaxTagCodec tagCodec;
    DaxDictionary dictionary;
    public DaxDictionaryMessageFactory(DaxTagCodec tagCodec, DaxDictionary dictionary){

        this.tagCodec = tagCodec;
        this.dictionary = dictionary;

    }


    private void putAttributesToTagBlock(DaxBody body, DaxTag tag, Map<DaxTag, DaxPair<?>> map){
        body.nextBlock(DaxBlockType.BLOCK_TAG);
        body.putPair(new DaxPairTag(ENTRY_TAG,tag));

        map.forEach((atrTag, pair) -> body.putPair(pair));

    }

    private void putAttributesToFieldBlock(DaxBody body,DaxTag entityTag ,DaxTag tag, Map<DaxTag, DaxPair<?>> map){
        body.nextBlock(DaxBlockType.BLOCK_FIELD);
        body.putPair(new DaxPairTag(ENTRY_OWNER_ID,entityTag));
        body.putPair(new DaxPairTag(ENTRY_TAG,tag));
        map.forEach((atrTag, pair) -> body.putPair(pair));

    }



    private   String createTagListStr(Set<DaxTag> daxFields){

        return daxFields.stream()
                .map(tag ->  tagCodec.encode(tag))
                .collect(Collectors.joining(DaxCoreConstants.TAG_LIST_SEPARATOR));
    }

    private   String createStringValueListStr(Set<String> stringSet){

        return String.join(DaxCoreConstants.TAG_LIST_SEPARATOR, stringSet);
    }



    private void putCollectionToBlock(DaxBody body,
            DaxTag tag,
            Map<DaxTag,DaxPair<?>> atrMap){
        body.nextBlock(DaxBlockType.BLOCK_COLLECTION);
//        body.nextBlock(DaxBlockType.BLOCK_TAG);
        body.putPair(ENTRY_TAG, tagCodec.encode(tag));
        atrMap.forEach((daxTag, pair) ->
                body.putPair(pair));

    }
    private void putCollectionValuesToBody(DaxBody body, DaxTag colTag, DaxBaseDictionary<String> values){

        values.getAttributMap().forEach((s, tagDaxPairMap) ->
                {
                    body.nextBlock(DaxBlockType.BLOCK_VALUE);
                    body.putPair(ENTRY_OWNER_ID, tagCodec.encode(colTag));
                    body.putPair(COLLECTION_VALUE, s);

//TODO put value list with descroiption  if description is empty
//            if (!value.getDesc().isBlank()) {
//                body.putPair(ENTRY_DESCRIPTION, value.getDesc());
//            }
                }
        );

    }

    private void collectionDictionaryToMsg(DaxBody body, DaxBaseDictionary<DaxTag> collectionDic ){

        collectionDic.getAttributMap().forEach((daxTag, tagDaxPairMap) ->
                { putCollectionToBlock(body,daxTag,tagDaxPairMap);
                    putCollectionValuesToBody(body,daxTag,  dictionary.getCollectionValues(daxTag));
                }
        );
    }



    private void putMsgItem(DaxBody body,  DaxMessageItem msgItem){
        body.nextBlock(DaxBlockType.BLOCK_MESSAGE);
        body.putPair(ENTRY_SYMBOL, msgItem.getMsgType());
        body.putPair(ENTRY_DESCRIPTION, msgItem.getMsgDesc());

        body.putPair(MESSAGE_TAGS, createTagListStr(msgItem.getMsgFields()));
        body.putPair(MESSAGE_RELATED_MSGS, createStringValueListStr(msgItem.getRelatedMsgType()));

    }

    private void putContextToBody(DaxBody body, DaxContext daxContext) {
        body.nextBlock(DaxBlockType.BLOCK_CONTEXT);
        body.putPair(ENTRY_SYMBOL, daxContext.getSymbol());
        body.putPair(FIELD_VALUE_PREFIX, daxContext.getTagPrefix());
        body.putPair(ENTRY_DESCRIPTION, daxContext.getDescription());

    }


    private void entityEntryToBlock(DaxBody body,DaxTag entityTag, DaxBaseDictionary<DaxTag> baseDic){
        baseDic.getAttributMap().forEach((tag, atrMap) ->
                putAttributesToFieldBlock(body, entityTag,tag, atrMap)
        );
    }

    private void putEntityToBody(DaxBody body, DaxTag entityTag){
        Set<DaxTag> entityTagSet = dictionary.getEntityFieldsMap().get(entityTag);

        body.nextBlock(DaxBlockType.BLOCK_ENTITY);
        body.putPair(ENTRY_TAG, tagCodec.encode(entityTag) );

        DaxBaseDictionary<DaxTag> baseDic = dictionary.getEntityBaseDic(entityTag);

//        baseDic.getAttributMap().forEach((daxTag, tagDaxPairMap) ->
//                body.putPair(
//        body.putPair(ENTRY_NAME, dictionary.getEntityEntryAttributes().  entity.getName());


//        if (!entity.getDescription().isBlank() ){
//            body.putPair(ENTRY_DESCRIPTION, entity.getDescription());
//        }

        body.putPair(TAG_LIST, createTagListStr(entityTagSet));



    }


    private void putTagBlockByDestiny(DaxTag tag, DaxTagDestiny destiny){



    }
    private void  putSchemaToBlock(DaxBody body,  Map<DaxTag, DaxPair<?>> tagDaxPairMap){
        body.nextBlock(DaxBlockType.BLOCK_SCHEMA);

        tagDaxPairMap.forEach((daxTag, pair) -> body.putPair(pair));

    }


    public DaxMessage dictionaryToMsg() {
        DaxMessage message = new DaxMessage(DaxCoreMessages.DATA_DIC);

        dictionary.getContextMap().forEach((idCtx, context) ->
                putContextToBody(message.getBody(), context)
        );

        dictionary.getMsgMap().forEach((s, messageDicItem) ->
                putMsgItem(message.getBody(),messageDicItem)
        );

        //TODO SCHEMA
        dictionary.getSchemaDictionary().getAttributMap()
                .forEach((integer, tagDaxPairMap) ->
                        putSchemaToBlock(message.getBody(), tagDaxPairMap)
                );


        dictionary.getTagAttributeMap().forEach((tag, atrMap) ->
                putAttributesToTagBlock(message.getBody(),tag,  atrMap)
        );



        dictionary.getTagDestinyMap().forEach(this::putTagBlockByDestiny);



        collectionDictionaryToMsg(message.getBody(),dictionary.getCollectionAttributes());


        dictionary.getEntityEntryAttributes()
                .forEach((entityTag, baseDic) ->
                        entityEntryToBlock(message.getBody(),entityTag, baseDic ));



        dictionary.getTagDestinyMap().forEach((tag, destiny) -> {
            if (DaxTagDestiny.ENTITY.equals(destiny)) {
                // This is safe even if destiny is null
                putEntityToBody(message.getBody(), tag);
            }
        });

//        dictionary.getEntityMap().forEach((tag, entity) ->
//                putEntityToBody(message.getBody(), entity, dictionary.getEntityFieldsMap().get(entity.getTag())));



        message.finish();
        return message;
    }


}
