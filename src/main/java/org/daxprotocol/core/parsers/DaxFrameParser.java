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

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.value.DaxValueString;
import org.daxprotocol.core.register.DaxRegister;
import org.daxprotocol.core.exceptions.DaxPreambleException;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.exceptions.DaxFrameParserException;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.value.DaxValue;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxChecksumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class DaxFrameParser {
    private static final Logger logger = LoggerFactory.getLogger(DaxFrameParser.class);

    DaxTagParser tagParser;
    DaxContextMapper contextMapper;
    DaxConfig config;
    DaxMessageFactory messageFactory;
    DaxPreambleCodec preambleCodec;

    //char[] separators = { DaxCoreConstants.DEFAULT_PAIR_SEPARATOR,'|','^','#'};
    public DaxFrameParser(DaxConfig config,
                            DaxContextMapper contextMapper,
                            DaxTagParser tagParser,
                            DaxRegister daxDic,
            DaxMessageFactory messageFactory,
            DaxPreambleCodec preambleCodec) {
        this.tagParser = tagParser;
        this.contextMapper = contextMapper;
        this.config = config;
        this.messageFactory = messageFactory;
        this.preambleCodec = preambleCodec;
    }

    private int findFirstSeparator(String input, char[] separators) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            for (char sep : separators) {
                if (c == sep) return i; // Found the first separator
            }
        }
        return -1;
    }
    private   List<Integer> getSeparatorIndices(String str, char pairSeparator) {
        if (str == null || str.isEmpty()) {
            throw new DaxFrameParserException("Frame is EMPTY !!!");
        }

        List<Integer> indexList = new ArrayList<>();

        // Loop through the string and find every occurrence
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == pairSeparator ) {
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
        char pairSeparator ; //= DaxCoreConstants.PAIR_SEPARATOR;
        int sepPos = findFirstSeparator(frameStr, DaxCoreConstants.ALLOWED_PAIR_SEPARATORS);

        if (sepPos > 0){
               pairSeparator = frameStr.charAt(sepPos);
        }
        else {
                                logger.error("1> IT IS NOT DAXP MESSAGE : {}", frameStr);
                    throw new DaxFrameParserException("IT IS NOT DAXP MESSAGE !!!");

        }


        List<Integer> indList = getSeparatorIndices(frameStr, pairSeparator);
        List<DaxPair> listOfPair =  new ArrayList<>();
        String tagStr;
        String valueStr;
        int sum = 0;

        int prevIdx = 0;
        int msgSize = frameStr.length();
        int inxSize = indList.size();
        int lastIdx = indList.get(inxSize - 1);
        boolean isPreableParsing = true;
        boolean isChecksumLast = true;

        DaxPreamble preamble = new DaxPreamble();
        preamble.setPairSeparator(pairSeparator);

        for (int idx : indList) {

            if (prevIdx == 0) {
                if (!frameStr.substring(prevIdx, idx).trim().equals(DaxCoreConstants.DAXP_SYMBOL)) {
                    logger.error("IT IS NOT DAXP MESSAGE : {}", frameStr);
                    throw new DaxFrameParserException("IT IS NOT DAXP MESSAGE !!!");
                }
                prevIdx = idx + 1;
                continue;
            }



            int equalChar = frameStr.substring(prevIdx, idx).indexOf('=') + prevIdx;
            if ( prevIdx > equalChar) {
                throw new DaxFrameParserException("IT IS ANY INCOMPATIBLE MESSAGE !!!");
            }


            tagStr = frameStr.substring(prevIdx, equalChar).trim();
            valueStr = frameStr.substring(equalChar + 1, idx);

            sum += DaxChecksumService.calculateSum(tagStr);
            sum += DaxChecksumService.calculateSum(valueStr);


            try {
                if (isPreableParsing && preambleCodec.isTagPreamble(tagStr)) {
                    preambleCodec.decodeTag(tagStr, valueStr ,preamble);

                } else {

                    DaxTag tag = tagParser.parseDaxTag(tagStr, preamble.getContextId());
                    if (workMode == 'P'){
                        break;
                    }

                    if ((isPreableParsing || isChecksumLast)
                            && !tag.equals(DaxCoreTags.MSG_TYPE)){
                        throw new DaxPreambleException("First tag is not MSG_TYPE");
                    }

                    isPreableParsing = false;
                    isChecksumLast = false;

                    if (tag.equals(DaxCoreTags.MSG_TYPE))
                    {
                        if (!listOfPair.isEmpty()){
                            listOfPair.clear();
                        }
                    }
                    //------------
                    //TODO  move to msgCodec and develop
                    DaxPair pair;
                    if(tag.equals(DaxCoreTags.REQ_FIELD_LIST)){
                        Set<DaxTag> daxTagSet =
                                Arrays.stream(valueStr.split(String.valueOf(DaxCoreConstants.TAG_LIST_SEPARATOR)))
                                        .map(String::trim)
                                        .map(s ->  tagParser.parseDaxTag(s,preamble.getContextId()))
                                        .collect(Collectors.toSet());
                        pair = new DaxPair(tag,daxTagSet);
                    }
                    else {
                        pair = new DaxPair(tag, new DaxValueString( valueStr ));
                    }

                    listOfPair.add(pair);

                    if (tag.equals(DaxCoreTags.CHECKSUM)){

                        int checksum = DaxChecksumService.calculateModulo(sum);

                        if(checksum!=Integer.parseInt(valueStr)){
                            logger.error("BAD CHECKSUM {} ,  correct is {} .", valueStr, checksum);
                        }
                        messageList.add(messageFactory.createMsg(listOfPair));
                        sum = 0;
                        isChecksumLast = true;
                    }


                }
            } catch (Exception e) {
                throw new DaxFrameParserException(e);
            }
            prevIdx = idx + 1;
        }

        if (workMode == 'M') {
            // Preamble message counter checking
            int mListSize = messageList.size();
            if (preamble.getMsgCnt() == -1) {
                if (mListSize>1){
                   logger.info("Preamble has not set message quantity .");
                }
                preamble.setMsgCnt(messageList.size());
            }

            if(!isChecksumLast){
                logger.error("CHECKSUM Not Ended Frame !!!");
                throw new DaxFrameParserException("Not correct FRAME !!!");
            }


            if (preamble.getMsgCnt() != messageList.size()) {
                logger.error("Preamble message counter {} ,real message {} ", +preamble.getMsgCnt(), messageList.size());
                throw new DaxFrameParserException("Not correct message number !!!");
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
