package org.daxprotocol.core.registries;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.tag.DaxTagDestiny;
import org.daxprotocol.core.parsers.DaxTagParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


//TODO DaxMessagePopulator joint  with message codec
public class DaxMessagePopulator {
    DaxTagParser tagParser;
    DaxSemanticRegistry daxDic;
    public DaxMessagePopulator(DaxTagParser tagParser, DaxSemanticRegistry daxDic){
        this.tagParser = tagParser;
        this.daxDic =  daxDic;
    }

    private void populateFromMsgBlock(int msgnamespaceId , Map<DaxTag, DaxPair<?>> blockPairMap) {

        Character blockType =   blockPairMap.get(DaxCoreTags.BLOCK_TYPE).getCharValue();

        if(blockType.equals(DaxBlockType.BLOCK_MESSAGE.getCode())){
            String msgDesc = "";
            if (blockPairMap.containsKey(DaxCoreTags.ENTRY_DESCRIPTION)){
                msgDesc =  blockPairMap.get(DaxCoreTags.ENTRY_DESCRIPTION).getStrValue();
            }

            DaxMessageItem msgItem = new DaxMessageItem(
                    blockPairMap.get(DaxCoreTags.ENTRY_SYMBOL).getStrValue(),msgDesc);

            if (blockPairMap.containsKey(DaxCoreTags.MESSAGE_TAGS)){

                tagParser.parseDaxTagList(
                        blockPairMap.get(DaxCoreTags.MESSAGE_TAGS)
                                .getStrValue(), msgnamespaceId)
                        .forEach(msgItem::addReqTag);

            }

            //TODO refactor : split change do byte after byte reading
            if (blockPairMap.containsKey(DaxCoreTags.MESSAGE_RELATED_MSGS)){

                Arrays.stream(blockPairMap.get(DaxCoreTags.MESSAGE_RELATED_MSGS)
                                .getStrValue().split(String.valueOf(DaxCoreConstants.TAG_LIST_SEPARATOR)))
                        .forEach(msgItem::addRelatedMsgType);

            }

            daxDic.putMsgItem(msgItem);

            return;
        }

//        if(blockType.equals(DaxBlockType.BLOCK_COLLECTION.getCode())){
//
//            DaxTag enumTag = tagParser.parseDaxTag(
//                    blockPairMap.get(DaxCoreTags.COLLECTION_ID).getStrValue() , msgnamespaceId
//            ) ;
//
//            daxDic.putTag( enumTag, DaxRegisterSource.MESSAGE, DaxTagDestiny.COLLECTION);
//
//
//            String name = blockPairMap.get(DaxCoreTags.ENTRY_NAME).getStrValue();
//            String desc = "";
//            if (blockPairMap.containsKey(DaxCoreTags.ENTRY_DESCRIPTION)){
//                desc = blockPairMap.get(DaxCoreTags.ENTRY_DESCRIPTION).getStrValue();
//            }
//      //????      daxDic.putEnum(enumTag, new DaxCollection_TMP(name , desc ));
//
//
//            return;
//        }

        if(blockType.equals(DaxBlockType.BLOCK_VALUE)){
            DaxTag enumTag = tagParser.parseDaxTag(
                    blockPairMap.get(DaxCoreTags.COLLECTION_ID).getStrValue() , msgnamespaceId
            ) ;

            String valueDesc = "";
            String value  = blockPairMap.get(DaxCoreTags.COLLECTION_VALUE).getStrValue();

            if (blockPairMap.containsKey(DaxCoreTags.ENTRY_DESCRIPTION)) {
                valueDesc = blockPairMap.get(DaxCoreTags.ENTRY_DESCRIPTION).getStrValue();
            }
      //????      daxDic.putEnumValue(enumTag, new DaxEnumValue(value, valueDesc));

            return;
        }



        if(blockType.equals(DaxBlockType.BLOCK_TAG)){

            DaxTag tag = tagParser.parseDaxTag(
                    blockPairMap.get(DaxCoreTags.ENTRY_TAG).getStrValue() , msgnamespaceId
            ) ;

            daxDic.putTag( tag, DaxRegisterSource.MESSAGE, DaxTagDestiny.TAG);
            daxDic.putTag( tag, DaxRegisterSource.MESSAGE, DaxTagDestiny.TAG);

            //TODO check if not exist FIELD_DATA_TYPE keep as String with warring

/*
            if(blockPairMap.containsKey(DaxCoreTags.ATR_DATA_TYPE)) {

                //TODO make validator nad filter
                //daxDic.putAtrDataType(tag, blockPairMap.get(DaxCoreTags.DATA_TYPE).getStrValue());


            }

            else if(blockPairMap.containsKey(DaxCoreTags.ENTITY_DATA_TYPE_ID)) {
                DaxTag tag3 = tagParser.parseDaxTag(
                        blockPairMap.get(DaxCoreTags.ENTITY_DATA_TYPE_ID).getStrValue(), msgnamespaceId);

                daxDic.putAtrEntityDataTypeId(tag, tag3);
            }
            else{
                System.out.println("No data type !!!!!! ");
            }

*/

            if(blockPairMap.containsKey(DaxCoreTags.ATR_NULLABLE)) {
                daxDic.putTagAtrNullable(tag,
                        blockPairMap.get(DaxCoreTags.ATR_NULLABLE).getCharValue()=='Y'
                );
            }


            if(blockPairMap.containsKey(DaxCoreTags.ATR_SIZE_MAX)) {
                daxDic.putTagAtrSizeMax(tag,
                        blockPairMap.get(DaxCoreTags.ATR_SIZE_MAX).getIntegerValue()
                );
            }

            if(blockPairMap.containsKey(DaxCoreTags.ATR_SIZE_MIN)) {
                daxDic.putTagAtrSizeMin(tag,
                        blockPairMap.get(DaxCoreTags.ATR_SIZE_MIN).getIntegerValue()
                );
            }
            if(blockPairMap.containsKey(DaxCoreTags.ATR_READONLY)) {
                daxDic.putTagAtrReadOnly(tag,
                        blockPairMap.get(DaxCoreTags.ATR_READONLY).getBooleanValue()
                );
            }

            if(blockPairMap.containsKey(DaxCoreTags.COLLECTION_ID)) {
                daxDic.putCollectionType(tag,
                        tagParser.parseDaxTag(blockPairMap.get(DaxCoreTags.COLLECTION_ID).getStrValue(), msgnamespaceId)
                );
            }


            return;
        }



        if(blockType.equals(DaxBlockType.BLOCK_ENTITY)){
            String groupName = blockPairMap.get(DaxCoreTags.ENTITY_NAME).getStrValue();

            //int groupId = groupMapper.getReferenceId(groupName);
            DaxTag groupTag = tagParser.parseDaxTag(
                    blockPairMap.get(DaxCoreTags.ENTRY_TAG).getStrValue(), msgnamespaceId
            ) ;

            //blockPairMap.get(DaxTagConst.FIELD).getStrValue();

            daxDic.putTag(groupTag, DaxRegisterSource.MESSAGE, DaxTagDestiny.ENTITY);
            String fieldIdStrList = blockPairMap.get(DaxCoreTags.TAG_LIST).getStrValue();
            List<DaxTag> tagList = tagParser.parseDaxTagList(fieldIdStrList, msgnamespaceId);
            tagList.forEach(tag -> daxDic.putEntityField(groupTag, tag));
            return;
        }


    }

    public void populate( DaxPreamble preamble, DaxMessage message) {
        int namespaceId = preamble.getNamespaceId();
        message.getBody().getAllBlockMap().forEach((integer, integerDaxPairMap) ->
                populateFromMsgBlock(namespaceId, integerDaxPairMap)
        );

    }
}
