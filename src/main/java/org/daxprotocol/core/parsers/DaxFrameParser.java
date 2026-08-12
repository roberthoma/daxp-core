/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxPairCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.exceptions.DaxFrameParserException;
import org.daxprotocol.core.exceptions.DaxPreambleException;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxChecksumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.daxprotocol.core.application.DaxCoreConstants.*;

public class DaxFrameParser {
    private static final Logger logger = LoggerFactory.getLogger(DaxFrameParser.class);

    private enum WorkMode {
        PREAMBLE_ONLY,
        FULL_FRAME
    }

    private final DaxTagParser tagParser;
    private final DaxConfig config;
    private final DaxMessageFactory messageFactory;
    private final DaxPreambleCodec preambleCodec;
    private final DaxPairCodec pairCodec;

    public DaxFrameParser(DaxConfig config,
            DaxTagParser tagParser,
            DaxMessageFactory messageFactory,
            DaxPreambleCodec preambleCodec,
            DaxPairCodec pairCodec) {
        this.config = config;
        this.tagParser = tagParser;
        this.messageFactory = messageFactory;
        this.preambleCodec = preambleCodec;
        this.pairCodec = pairCodec;
    }

    private int findFirstSeparator(String input, char[] separators) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            for (char sep : separators) {
                if (c == sep) return i;
            }
        }
        return -1;
    }

    private List<Integer> getSeparatorIndices(String str, char pairSeparator) {
        if (str == null || str.isEmpty()) {
            throw new DaxFrameParserException("Frame is EMPTY!");
        }

        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == pairSeparator) {
                indexList.add(i);
            }
        }
        return indexList;
    }

    public DaxPreamble parsePreamble(String msg) {
        DaxFrame frame = parse(msg, WorkMode.PREAMBLE_ONLY);
        return frame.getPreamble();
    }

    public DaxFrame parseFrame(String msg) {
        return parse(msg, WorkMode.FULL_FRAME);
    }

    private DaxFrame parse(String frameStr, WorkMode workMode) {
        DaxFrame frame = new DaxFrame();
        List<DaxMessage> messageList = new ArrayList<>();

        int sepPos = findFirstSeparator(frameStr, DaxCoreConstants.ALLOWED_PAIR_SEPARATORS);
        if (sepPos <= 0) {
            logger.error("Invalid DAXP message prefix: {}", frameStr);
            throw new DaxFrameParserException("Invalid DAXP message prefix!");
        }

        char pairSeparator = frameStr.charAt(sepPos);
        List<Integer> indList = getSeparatorIndices(frameStr, pairSeparator);
        List<DaxPair<?>> listOfPair = new ArrayList<>();

        int sum = 0;
        int prevIdx = 0;
        boolean isPreambleParsing = true;
        boolean isChecksumLast = true;

        DaxPreamble preamble = new DaxPreamble();
        preamble.setPairSeparator(pairSeparator);

        for (int idx : indList) {

            // Header Check (First Token)
            if (prevIdx == 0) {
                String header = frameStr.substring(prevIdx, idx).trim();
                if (!header.equals(DaxCoreConstants.DAXP_SYMBOL)) {
                    logger.error("Not a DAXP message, expected header '{}': {}", DaxCoreConstants.DAXP_SYMBOL, frameStr);
                    throw new DaxFrameParserException("Invalid DAXP message header!");
                }
                prevIdx = idx + 1;
                continue;
            }

            // Locate Operator (=, Block, or Action)
            int operatorIdx = -1;
            char foundOperator = 0;
            for (int i = prevIdx; i < idx; i++) {
                char c = frameStr.charAt(i);
                if (c == OPERATOR_EQUAL || c == OPERATOR_BLOCK_REFERENCE || c == OPERATOR_ACTION) {
                    operatorIdx = i;
                    foundOperator = c;
                    break;
                }
            }

            if (operatorIdx == -1 || prevIdx > operatorIdx) {
                throw new DaxFrameParserException("Incompatible or missing operator in DAXP message tag!");
            }

            String tagStr = frameStr.substring(prevIdx, operatorIdx).trim();
            String valueStr = frameStr.substring(operatorIdx + 1, idx);

            try {
                if (isPreambleParsing && preambleCodec.isTagPreamble(tagStr)) {
                    preambleCodec.decodeTag(tagStr, valueStr, preamble);
                } else {
                    DaxTag tag = tagParser.parseDaxTag(tagStr, preamble.getNamespaceId());

                    if (workMode == WorkMode.PREAMBLE_ONLY) {
                        break;
                    }

                    if ((isPreambleParsing || isChecksumLast) && !tag.equals(DaxCoreTags.MSG_TYPE)) {
                        throw new DaxPreambleException("First tag in message is not MSG_TYPE");
                    }

                    isPreambleParsing = false;
                    isChecksumLast = false;

                    if (tag.equals(DaxCoreTags.MSG_TYPE)) {
                        listOfPair.clear();
                    }

                    DaxPair<?> pair = pairCodec.decode(tag, valueStr, foundOperator, preamble.getNamespaceId());
                    listOfPair.add(pair);

                    if (tag.equals(DaxCoreTags.CHECKSUM)) {
                        int expectedChecksum = DaxChecksumService.calculateModulo(sum);
                        int actualChecksum = Integer.parseInt(valueStr);

                        if (expectedChecksum != actualChecksum) {
                            logger.error("BAD CHECKSUM {}. Expected checksum is {}.", actualChecksum, expectedChecksum);
                            throw new DaxFrameParserException("Checksum validation failed!");
                        }

                        // Create message with a defensive copy of pairs
                        messageList.add(messageFactory.createMsg(new ArrayList<>(listOfPair)));
                        listOfPair.clear();
                        sum = 0;
                        isChecksumLast = true;
                    } else {
                        // Accumulate checksum sum for payload tags (excluding CHECKSUM tag itself)
                        sum += DaxChecksumService.calculateSum(tagStr);
                        sum += DaxChecksumService.calculateSum(valueStr);
                    }
                }
            } catch (Exception e) {
                if (e instanceof DaxFrameParserException) {
                    throw (DaxFrameParserException) e;
                }
                throw new DaxFrameParserException("Error parsing frame body", e);
            }

            prevIdx = idx + 1;
        }

        if (workMode == WorkMode.FULL_FRAME) {
            if (preamble.getMsgCnt() == -1) {
                if (messageList.size() > 1) {
                    logger.info("Preamble did not specify message quantity.");
                }
                preamble.setMsgCnt(messageList.size());
            }

            if (!isChecksumLast) {
                logger.error("Frame does not end with CHECKSUM!");
                throw new DaxFrameParserException("Malformed frame: Missing trailing CHECKSUM tag!");
            }

            if (preamble.getMsgCnt() != messageList.size()) {
                logger.error("Preamble message count {} does not match actual count {}", preamble.getMsgCnt(), messageList.size());
                throw new DaxFrameParserException("Message count mismatch against preamble!");
            }

            frame.setMessageList(messageList);
        }

        frame.setPreamble(preamble);
        return frame;
    }
}