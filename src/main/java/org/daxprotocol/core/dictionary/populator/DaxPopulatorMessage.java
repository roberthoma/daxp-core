package org.daxprotocol.core.dictionary.populator;

import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxEnum;
import org.daxprotocol.core.dictionary.DaxEnumValue;
import org.daxprotocol.core.dictionary.DaxMessageItem;
import org.daxprotocol.core.field.DaxBlockType;
import org.daxprotocol.core.dto.DaxDTO;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxParserService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DaxPopulatorMessage {
    DaxParserService parserService;
    public DaxPopulatorMessage(DaxParserService parserService){
        this.parserService = parserService;
    }


    private void populateFromMsgBlock(DaxDictionary daxDic,int msgContextId , Map<DaxTag, DaxPair<?>> blockPairMap) {

        String blockType =   blockPairMap.get(DaxTagConst.BLOCK_TYPE).getStrValue();

        if(blockType.equals(DaxBlockType.BLOCK_MESSAGE)){
            String msgDesc = "";
            if (blockPairMap.containsKey(DaxTagConst.FIELD_VALUE_DESCRIPTION)){
                msgDesc =  blockPairMap.get(DaxTagConst.FIELD_VALUE_DESCRIPTION).getStrValue();
            }

            DaxMessageItem msgItem = new DaxMessageItem(
                    blockPairMap.get(DaxTagConst.FIELD_VALUE).getStrValue(),msgDesc);

            if (blockPairMap.containsKey(DaxTagConst.MESSAGE_TAGS)){

                parserService.parseDaxTagList(
                        blockPairMap.get(DaxTagConst.MESSAGE_TAGS)
                                .getStrValue(), msgContextId)
                        .forEach(msgItem::addReqTag);

            }

            //TODO refactor : split change do byte after byte reading
            if (blockPairMap.containsKey(DaxTagConst.MESSAGE_RELATED_MSGS)){
                Arrays.stream(blockPairMap.get(DaxTagConst.MESSAGE_RELATED_MSGS)
                                .getStrValue().split(String.valueOf(DaxConfig.TAG_LIST_SEPARATOR)))
                        .forEach(msgItem::addRelatedMsgType);
            }

            daxDic.putMsgItem(msgItem);

            return;
        }

        if(blockType.equals(DaxBlockType.BLOCK_ENUM)){

            DaxTag enumTag = parserService.parseDaxTag(
                    blockPairMap.get(DaxTagConst.ENUM_ID).getStrValue() , msgContextId
            ) ;

            daxDic.putTag( enumTag);


            String name = blockPairMap.get(DaxTagConst.ENUM_NAME).getStrValue();
            String desc = "";
            if (blockPairMap.containsKey(DaxTagConst.ENUM_DESCRIPTION)){
                desc = blockPairMap.get(DaxTagConst.ENUM_DESCRIPTION).getStrValue();
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
            DaxTag enumTag = parserService.parseDaxTag(
                    blockPairMap.get(DaxTagConst.ENUM_ID).getStrValue() , msgContextId
            ) ;

            String valueDesc = "";
            String value  = blockPairMap.get(DaxTagConst.ENUM_VALUE).getStrValue();

            if (blockPairMap.containsKey(DaxTagConst.ENUM_VALUE_DESCRIPTION)) {
                valueDesc = blockPairMap.get(DaxTagConst.ENUM_VALUE_DESCRIPTION).getStrValue();
            }
            daxDic.putEnumValue(enumTag, new DaxEnumValue(value, valueDesc));

            return;
        }



        if(blockType.equals(DaxBlockType.BLOCK_TAG)){

            DaxTag tag = parserService.parseDaxTag(
                    blockPairMap.get(DaxTagConst.FIELD_ID).getStrValue() , msgContextId
            ) ;

            daxDic.putTag( tag);

            //TODO check if not exist FIELD_DATA_TYPE keep as String with warring


            if(blockPairMap.containsKey(DaxTagConst.FIELD_DATA_TYPE)) {
                daxDic.putAtrDataType(tag, blockPairMap.get(DaxTagConst.FIELD_DATA_TYPE).getCharValue());
            }

            else if(blockPairMap.containsKey(DaxTagConst.DTO_DATA_TYPE_ID)) {
                DaxTag tag3 = parserService.parseDaxTag(
                        blockPairMap.get(DaxTagConst.DTO_DATA_TYPE_ID).getStrValue(), msgContextId);
                daxDic.putAtrDtoDataTypeId(tag, tag3);
            }
            else{
                System.out.println("No data type !!!!!! ");
            }


            if(blockPairMap.containsKey(DaxTagConst.ATR_UI_LABEL)) {
                daxDic.putAtrUiLabel(tag, blockPairMap.get(DaxTagConst.ATR_UI_LABEL).getStrValue());

            }

            if(blockPairMap.containsKey(DaxTagConst.ATR_NULLABLE)) {
                daxDic.putAtrNullable(tag,
                        blockPairMap.get(DaxTagConst.ATR_NULLABLE).getCharValue()=='Y'
                );
            }


            if(blockPairMap.containsKey(DaxTagConst.ATR_SIZE_MAX)) {
                daxDic.putAtrSizeMax(tag,
                        blockPairMap.get(DaxTagConst.ATR_SIZE_MAX).getIntegerValue()
                );
            }

            if(blockPairMap.containsKey(DaxTagConst.ATR_SIZE_MIN)) {
                daxDic.putAtrSizeMin(tag,
                        blockPairMap.get(DaxTagConst.ATR_SIZE_MIN).getIntegerValue()
                );
            }
            if(blockPairMap.containsKey(DaxTagConst.ATR_READONLY)) {
                daxDic.putAtrReadOnly(tag,
                        blockPairMap.get(DaxTagConst.ATR_READONLY).getBooleanValue()
                );
            }

            if(blockPairMap.containsKey(DaxTagConst.ENUM_ID)) {
                daxDic.putAtrEnumTypeTag(tag,
                        parserService.parseDaxTag(blockPairMap.get(DaxTagConst.ENUM_ID).getStrValue(), msgContextId)
                );
            }


            return;
        }



        if(blockType.equals(DaxBlockType.BLOCK_DTO)){
            String groupName = blockPairMap.get(DaxTagConst.GROUP_NAME).getStrValue();

            //int groupId = groupMapper.getReferenceId(groupName);
            DaxTag groupTag = parserService.parseDaxTag(
                    blockPairMap.get(DaxTagConst.FIELD_ID).getStrValue(), msgContextId
            ) ;

            //blockPairMap.get(DaxTagConst.FIELD).getStrValue();

            DaxDTO group = new DaxDTO(groupTag,groupName);
            daxDic.putDTO(group);
            String fieldIdStrList = blockPairMap.get(DaxTagConst.FIELD_ID_LIST).getStrValue();
            List<DaxTag> tagList = parserService.parseDaxTagList(fieldIdStrList, msgContextId);
            tagList.forEach(tag -> daxDic.putDtoField(groupTag, tag));
            return;
        }


    }

    public void populate(DaxDictionary daxDic,  DaxPreamble preamble, DaxMessage message) {
        int contextId = preamble.getMsgContextId();
        message.getBody().getBlockMap().forEach((integer, integerDaxPairMap) ->
                populateFromMsgBlock(daxDic, contextId, integerDaxPairMap)
        );

    }
}
