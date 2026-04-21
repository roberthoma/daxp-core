package org.daxprotocol.core.register;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxEnum;
import org.daxprotocol.core.dictionary.DaxEnumValue;
import org.daxprotocol.core.dictionary.DaxMessageItem;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.dto.DaxDTO;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxTagParser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DaxPopulatorMessage {
    DaxTagParser tagParser;
    DaxDictionary daxDic;
    public DaxPopulatorMessage(DaxTagParser tagParser, DaxDictionary daxDic){
        this.tagParser = tagParser;
        this.daxDic =  daxDic;
    }

    public List<DaxTag> parseDaxTagList (String tagListStr, int msgContextId){

        return Arrays.stream(tagListStr.split(String.valueOf(DaxCoreConstants.TAG_LIST_SEPARATOR)))
                .map(String::trim)
                .map(s ->  tagParser.parseDaxTag(s,msgContextId))
                .collect(Collectors.toList());
    }
    private void populateFromMsgBlock(int msgContextId , Map<DaxTag, DaxPair<?>> blockPairMap) {

        String blockType =   blockPairMap.get(DaxCoreTags.BLOCK_TYPE).getStrValue();

        if(blockType.equals(DaxBlockType.BLOCK_MESSAGE)){
            String msgDesc = "";
            if (blockPairMap.containsKey(DaxCoreTags.FIELD_VALUE_DESCRIPTION)){
                msgDesc =  blockPairMap.get(DaxCoreTags.FIELD_VALUE_DESCRIPTION).getStrValue();
            }

            DaxMessageItem msgItem = new DaxMessageItem(
                    blockPairMap.get(DaxCoreTags.FIELD_VALUE).getStrValue(),msgDesc);

            if (blockPairMap.containsKey(DaxCoreTags.MESSAGE_TAGS)){

                tagParser.parseDaxTagList(
                        blockPairMap.get(DaxCoreTags.MESSAGE_TAGS)
                                .getStrValue(), msgContextId)
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

        if(blockType.equals(DaxBlockType.BLOCK_ENUM)){

            DaxTag enumTag = tagParser.parseDaxTag(
                    blockPairMap.get(DaxCoreTags.ENUM_ID).getStrValue() , msgContextId
            ) ;

            daxDic.putTag( enumTag);


            String name = blockPairMap.get(DaxCoreTags.ENUM_NAME).getStrValue();
            String desc = "";
            if (blockPairMap.containsKey(DaxCoreTags.ENUM_DESCRIPTION)){
                desc = blockPairMap.get(DaxCoreTags.ENUM_DESCRIPTION).getStrValue();
            }
            daxDic.putEnum(enumTag, new DaxEnum(name , desc ));

            //TODO Add this implementation
//            String valuesStrList = blockPairMap.get(DaxTagConst.ENUM_VALUE_LIST).getStrValue();
//            List<String>  valueList =  Arrays.stream(valuesStrList
//                                                     .split(DaxpConfig.VALUE_LIST_SEPARATOR.toString()))
//                    .map(String::trim)
//                    .filter(s -> !s.isEmpty())
//                    .collect(Collectors.toList());
//             valueList.forEach(eValue -> daxDic.putEnumValue( enumTag, new DaxEnumValue(eValue , "")));

            return;
        }

        if(blockType.equals(DaxBlockType.BLOCK_ENUM_VALUE)){
            DaxTag enumTag = tagParser.parseDaxTag(
                    blockPairMap.get(DaxCoreTags.ENUM_ID).getStrValue() , msgContextId
            ) ;

            String valueDesc = "";
            String value  = blockPairMap.get(DaxCoreTags.ENUM_VALUE).getStrValue();

            if (blockPairMap.containsKey(DaxCoreTags.ENUM_VALUE_DESCRIPTION)) {
                valueDesc = blockPairMap.get(DaxCoreTags.ENUM_VALUE_DESCRIPTION).getStrValue();
            }
            daxDic.putEnumValue(enumTag, new DaxEnumValue(value, valueDesc));

            return;
        }



        if(blockType.equals(DaxBlockType.BLOCK_TAG)){

            DaxTag tag = tagParser.parseDaxTag(
                    blockPairMap.get(DaxCoreTags.FIELD_ID).getStrValue() , msgContextId
            ) ;

            daxDic.putTag( tag);

            //TODO check if not exist FIELD_DATA_TYPE keep as String with warring


            if(blockPairMap.containsKey(DaxCoreTags.DATA_TYPE)) {
                daxDic.putAtrDataType(tag, blockPairMap.get(DaxCoreTags.DATA_TYPE).getStrValue());
            }

            else if(blockPairMap.containsKey(DaxCoreTags.DTO_DATA_TYPE_ID)) {
                DaxTag tag3 = tagParser.parseDaxTag(
                        blockPairMap.get(DaxCoreTags.DTO_DATA_TYPE_ID).getStrValue(), msgContextId);
                daxDic.putAtrDtoDataTypeId(tag, tag3);
            }
            else{
                System.out.println("No data type !!!!!! ");
            }


            if(blockPairMap.containsKey(DaxCoreTags.ATR_UI_LABEL)) {
                daxDic.putAtrUiLabel(tag, blockPairMap.get(DaxCoreTags.ATR_UI_LABEL).getStrValue());

            }

            if(blockPairMap.containsKey(DaxCoreTags.ATR_NULLABLE)) {
                daxDic.putAtrNullable(tag,
                        blockPairMap.get(DaxCoreTags.ATR_NULLABLE).getCharValue()=='Y'
                );
            }


            if(blockPairMap.containsKey(DaxCoreTags.ATR_SIZE_MAX)) {
                daxDic.putAtrSizeMax(tag,
                        blockPairMap.get(DaxCoreTags.ATR_SIZE_MAX).getIntegerValue()
                );
            }

            if(blockPairMap.containsKey(DaxCoreTags.ATR_SIZE_MIN)) {
                daxDic.putAtrSizeMin(tag,
                        blockPairMap.get(DaxCoreTags.ATR_SIZE_MIN).getIntegerValue()
                );
            }
            if(blockPairMap.containsKey(DaxCoreTags.ATR_READONLY)) {
                daxDic.putAtrReadOnly(tag,
                        blockPairMap.get(DaxCoreTags.ATR_READONLY).getBooleanValue()
                );
            }

            if(blockPairMap.containsKey(DaxCoreTags.ENUM_ID)) {
                daxDic.putAtrEnumTypeTag(tag,
                        tagParser.parseDaxTag(blockPairMap.get(DaxCoreTags.ENUM_ID).getStrValue(), msgContextId)
                );
            }


            return;
        }



        if(blockType.equals(DaxBlockType.BLOCK_DTO)){
            String groupName = blockPairMap.get(DaxCoreTags.DTO_NAME).getStrValue();

            //int groupId = groupMapper.getReferenceId(groupName);
            DaxTag groupTag = tagParser.parseDaxTag(
                    blockPairMap.get(DaxCoreTags.FIELD_ID).getStrValue(), msgContextId
            ) ;

            //blockPairMap.get(DaxTagConst.FIELD).getStrValue();

            DaxDTO group = new DaxDTO(groupTag,groupName);
            daxDic.putDTO(group);
            String fieldIdStrList = blockPairMap.get(DaxCoreTags.TAG_LIST).getStrValue();
            List<DaxTag> tagList = tagParser.parseDaxTagList(fieldIdStrList, msgContextId);
            tagList.forEach(tag -> daxDic.putDtoField(groupTag, tag));
            return;
        }


    }

    public void populate( DaxPreamble preamble, DaxMessage message) {
        int contextId = preamble.getContextId();
        message.getBody().getBlockMap().forEach((integer, integerDaxPairMap) ->
                populateFromMsgBlock(contextId, integerDaxPairMap)
        );

    }
}
