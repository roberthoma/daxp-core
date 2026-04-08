package org.daxprotocol.core.parsers;

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dispatcher.DaxFrame;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.exceptions.DaxMsgParserException;
import org.daxprotocol.core.field.DaxDataType;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.populator.DaxPopulatorAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DaxMessageParser {
    private static final Logger logger = LoggerFactory.getLogger(DaxMessageParser.class);

    DaxParserTag tagParser;
    DaxContextMapper contextMapper;
    DaxDictionary daxDic;
    DaxConfig config;
    DaxMessageCodec messageCodec;
    DaxPreambleCodec preambleCodec;
    public DaxMessageParser(DaxConfig config,
                            DaxContextMapper contextMapper,
                            DaxParserTag tagParser,
                            DaxDictionary daxDic, //,
                            DaxMessageCodec messageCodec) {
        this.tagParser = tagParser;
        this.contextMapper = contextMapper;
        this.daxDic = daxDic;
        this.config = config;
        this.messageCodec = messageCodec;
        this.preambleCodec = messageCodec.getPreambleCodec();
    }

    private   List<Integer> getPipeIndices(String str) {
        if (str == null || str.isEmpty()) {
            return null; //THROW Exception
        }

        List<Integer> indexList = new ArrayList<>();

        // Loop through the string and find every occurrence
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == DaxConfig.PAIR_SEPARATOR) {
                indexList.add(i);
            }
        }
        return indexList;
    }

    public DaxPreamble parsePreamble(String msg) {
        DaxFrame frame = parse(msg,'P');
        return frame.getPreamble();
    }

    @SuppressWarnings("unchecked")
    public DaxFrame parseFrame(String msg) {
        return parse(msg,'M');
    }


    //--------------------------
    private DaxFrame parse(String msgStr, char workMode) {
        DaxFrame frame = new DaxFrame();
        List<DaxMessage> messageList = new ArrayList<>();
        List<Integer> indList = getPipeIndices(msgStr);
        List<DaxPair<?>> listOfPair =  new ArrayList<>();

        int prevIdx = 0;
        int msgSize = msgStr.length();
        int inxSize = indList.size();
        int lastIdx = indList.get(inxSize - 1);
        boolean isPreableParsing = true;

        DaxPreamble preamble = new DaxPreamble();


        for (int idx : indList) {


            int equalChar = msgStr.substring(prevIdx, idx).indexOf('=') + prevIdx;
            if (prevIdx > equalChar) {
                throw new DaxMsgParserException("IT IS ANY INCOMPATIBLE MESSAGE !!!");
            }


            String tagStr = msgStr.substring(prevIdx, equalChar);
            if (prevIdx == 0) {
                if (!tagStr.trim().equals(DaxConfig.DAXP_SYMBOL)) {
                    logger.error("IT IS NOT DAXP MESSAGE : {}", msgStr);
                    throw new DaxMsgParserException("IT IS NOT DAXP MESSAGE !!!");
                }
            }
            String valueStr = msgStr.substring(equalChar + 1, idx);
            try {
                if (isPreableParsing && preambleCodec.isTagPreamble(tagStr)) {
                    preambleCodec.decodeTag(tagStr, valueStr ,preamble);
                } else {

                    DaxTag tag = tagParser.parseDaxTag(tagStr, config.getAppContextId());
                    isPreableParsing = false;
                    if (workMode == 'P'){
                        break;
                    }
                    if (tag.equals(DaxTagConst.MSG_TYPE))
                    {
                        if (!listOfPair.isEmpty()){
                            listOfPair.clear();
                        }
                    }
                    listOfPair.add(new DaxPair<>(tag,valueStr ));

                    if (tag.equals(DaxTagConst.CHECKSUM)){
                        messageList.add(messageCodec.createMsg(listOfPair));
                    }

                }
            } catch (Exception e) {
                throw new DaxMsgParserException(e);
            }
            prevIdx = idx + 1;
        }
        frame.setPreamble(preamble);
        frame.setMessageList(messageList);
        return frame;
    }

}
