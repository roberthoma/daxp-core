package org.daxprotocol.core.parsers;

import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.exceptions.DaxMsgParserException;
import org.daxprotocol.core.exceptions.DaxPreambleException;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleTag;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.ArrayList;
import java.util.List;

public class DaxParserMessage {

    DaxParserTag tagParser;
    DaxContextMapper contextMapper;
    DaxDictionary daxDic;
    DaxConfig config;
    public DaxParserMessage(DaxConfig config,
                            DaxContextMapper contextMapper,
                            DaxParserTag tagParser,
                            DaxDictionary daxDic) {
        this.tagParser = tagParser;
        this.contextMapper = contextMapper;
        this.daxDic = daxDic;
        this.config = config;
    }

    private   List<Integer> getPipeIndices(String str) {
        if (str == null || str.isEmpty()) {
            return null; //THROW Exception
        }

        List<Integer> indexList = new ArrayList<>();

        // Loop through the string and find every occurrence
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '|') {
                indexList.add(i);
            }
        }
        return indexList;
    }

    public DaxPreamble parsePreamble(String msg) {
        return (DaxPreamble)parse(msg,'P');
    }

    @SuppressWarnings("unchecked")
    public List<DaxMessage> parseMessageList(String msg) {
        try {
            return (List<DaxMessage>) parse(msg, 'M');
        } catch (Exception e) {
            throw new DaxMsgParserException(e);
        }
    }
    //--------------------------
    private Object parse(String msgStr, char workMode) {
        List<DaxMessage> messageList = new ArrayList<>();
        String daxpSymbol;
        List<Integer> indList = getPipeIndices(msgStr);
        List<DaxPair<?>> listOfPair ;

        int prevIdx = 0;
        int msgSize = msgStr.length();
        int inxSize = indList.size();
        int lastIdx = indList.get(inxSize - 1);
        boolean isPreableParsing = true;

        DaxPreamble preamble = new DaxPreamble();

  //     if (workMode == 'M'){
            listOfPair = new ArrayList<>();
    //    }



        for (int idx : indList) {


            int equalChar = msgStr.substring(prevIdx, idx).indexOf('=') + prevIdx;
            if (prevIdx > equalChar) {
                throw new DaxMsgParserException("IT IS ANY INCOMPATIBLE MESSAGE !!!");
            }


            String tagStr = msgStr.substring(prevIdx, equalChar);
            if (prevIdx == 0) {
                daxpSymbol = tagStr;
                if (!daxpSymbol.trim().equals(DaxConfig.DAXP_SYMBOL)) {
                    throw new DaxMsgParserException("IT IS NOT DAXP MESSAGE !!!");
                }
            }

            String valueStr = msgStr.substring(equalChar + 1, idx);

            System.out.println(">" + tagStr + "<:>" + valueStr + "<");

            try {
                if (isPreableParsing && DaxPreambleTag.contains(tagStr)) {
                    if (DaxPreambleTag.contains(tagStr)) {
                        DaxPreambleTag tag = DaxPreambleTag.fromTag(tagStr);
                        switch (tag) {
                            case DAXP -> System.out.println("Protocol identified");
                            case ENCODING    -> DaxCharacterEncoding.fromName(valueStr).ifPresent(preamble::setEncoding);
                            case MSG_COUNT   -> preamble.setMsgCnt(Integer.parseInt(valueStr));
                            case MSG_CONTEXT -> preamble.setMsgContextId(contextMapper.getReferenceId(valueStr));
                            //case MSG_SENDER  -> preamble.setSe System.out.println("Sender: " + value);
                        }
                    }
                    else {
                        throw new DaxPreambleException("Invalid tag: "+tagStr);
                    }


                } else {

                    if (workMode == 'P'){
                        return preamble;
                    }
                    isPreableParsing = false; ///Only preamble then finish

                    DaxTag tag = tagParser.parseDaxTag(tagStr, config.getAppContextId());
                    if (tag.equals(DaxTagConst.MSG_TYPE))
                    {
                        if (!listOfPair.isEmpty()){
                           //create message from list
                            listOfPair.clear();
                        }
                        listOfPair.add(new DaxPair<>(tag,valueStr ));
                    }

                    if (tag.equals(DaxTagConst.CHECKSUM)){
                        //create message from list
                        ????
                    }



                    System.out.println(" JEST TAG : ctxId=" + tag.getContextId() + " tagId=" + tag.getTagId());

                }
            } catch (Exception e) {
                throw new DaxMsgParserException(e);
            }

            prevIdx = idx + 1;

        }
        System.out.println("-------------------------------");

        return messageList;
    }


////////////////////////////////////////////////////////

}
