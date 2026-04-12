/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***********************************************************************
 */


package org.daxprotocol.core.parsers;

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.exceptions.DaxMsgParserException;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DaxFrameParser {
    private static final Logger logger = LoggerFactory.getLogger(DaxFrameParser.class);

    DaxTagParser tagParser;
    DaxContextMapper contextMapper;
    DaxConfig config;
    DaxMessageCodec messageCodec;
    DaxPreambleCodec preambleCodec;
    public DaxFrameParser(DaxConfig config,
                            DaxContextMapper contextMapper,
                            DaxTagParser tagParser,
                            DaxDictionary daxDic, //,
                            DaxMessageCodec messageCodec,
            DaxPreambleCodec preambleCodec) {
        this.tagParser = tagParser;
        this.contextMapper = contextMapper;
        this.config = config;
        this.messageCodec = messageCodec;
        this.preambleCodec = preambleCodec;
    }

    private   List<Integer> getPipeIndices(String str) {
        if (str == null || str.isEmpty()) {
            throw new DaxMsgParserException("Message is EMPTY !!!");
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
    private DaxFrame parse(String frameStr, char workMode) {
        DaxFrame frame = new DaxFrame();
        List<DaxMessage> messageList = new ArrayList<>();
        List<Integer> indList = getPipeIndices(frameStr);
        List<DaxPair<?>> listOfPair =  new ArrayList<>();
        String tagStr;
        String valueStr;

        int prevIdx = 0;
        int msgSize = frameStr.length();
        int inxSize = indList.size();
        int lastIdx = indList.get(inxSize - 1);
        boolean isPreableParsing = true;

        DaxPreamble preamble = new DaxPreamble(config.getDefaultEncoding());


        for (int idx : indList) {


            int equalChar = frameStr.substring(prevIdx, idx).indexOf('=') + prevIdx;
            if (prevIdx > equalChar) {
                throw new DaxMsgParserException("IT IS ANY INCOMPATIBLE MESSAGE !!!");
            }


            tagStr = frameStr.substring(prevIdx, equalChar);
            valueStr = frameStr.substring(equalChar + 1, idx);

            if (prevIdx == 0) {
                if (!tagStr.trim().equals(DaxConfig.DAXP_SYMBOL)) {
                    logger.error("IT IS NOT DAXP MESSAGE : {}", frameStr);
                    throw new DaxMsgParserException("IT IS NOT DAXP MESSAGE !!!");
                }
            }

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
                    //------------
                    //TODO  move to msgCodec
                    DaxPair<?> pair;
                    if(tag.equals(DaxTagConst.REQ_FIELD_LIST)){
                        Set<DaxTag> daxTagSet =
                                Arrays.stream(valueStr.split(String.valueOf(DaxConfig.TAG_LIST_SEPARATOR)))
                                        .map(String::trim)
                                        .map(s ->  tagParser.parseDaxTag(s,preamble.getMsgContextId()))
                                        .collect(Collectors.toSet());
                        pair = new DaxPair<Set<DaxTag>>(tag,daxTagSet);
                    }
                    else {
                        pair = new DaxPair<>(tag,valueStr );
                    }

                    listOfPair.add(pair);

                    if (tag.equals(DaxTagConst.CHECKSUM)){
                        messageList.add(messageCodec.createMsg(listOfPair));
                    }


                }
            } catch (Exception e) {
                throw new DaxMsgParserException(e);
            }
            prevIdx = idx + 1;
        }

        if (workMode != 'P') {
            // Preamble message counter checking
            int mListSize = messageList.size();
            if (preamble.getMsgCnt() == -1) {
                if (mListSize>1){
                   logger.info("Preamble has not set message number .");
                }
                preamble.setMsgCnt(messageList.size());
            }

            if (preamble.getMsgCnt() != messageList.size()) {
                logger.error("Preamble message counter {} ,real message {} ", +preamble.getMsgCnt(), messageList.size());
                throw new DaxMsgParserException("Not correct message number !!!");
            }
            frame.setMessageList(messageList);
        }
        frame.setPreamble(preamble);
        return frame;
    }



    public DaxFrame parseFromMap(Map<String, String> params) {
        throw new RuntimeException("parseFromMap not implemented jet !!!");
    }
}
