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
import org.daxprotocol.core.namespace.DaxNamespaceConfig;
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

import static org.daxprotocol.core.application.DaxCoreConstants.OPERATOR_ACTION;
import static org.daxprotocol.core.application.DaxCoreConstants.OPERATOR_REFERENCE;
import static org.daxprotocol.core.application.DaxCoreConstants.OPERATOR_EQUAL;
import static org.daxprotocol.core.application.DaxCoreConstants.ESCAPE_CHAR;

public class DaxFrameParser {
    private static final Logger logger = LoggerFactory.getLogger(DaxFrameParser.class);

    private enum WorkMode {
        PREAMBLE_ONLY,
        FULL_FRAME
    }

    private final DaxTagParser tagParser;
    private final DaxMessageFactory messageFactory;
    private final DaxPreambleParser preambleParser;
    private final DaxPairCodec pairCodec;

    public DaxFrameParser(DaxNamespaceConfig config,
            DaxTagParser tagParser,
            DaxMessageFactory messageFactory,
            DaxPreambleParser preambleParser,
            DaxPairCodec pairCodec) {
        this.tagParser = tagParser;
        this.messageFactory = messageFactory;
        this.preambleParser = preambleParser;
        this.pairCodec = pairCodec;
    }

    public DaxPreamble parsePreamble(String msg) {
        return parse(msg, WorkMode.PREAMBLE_ONLY).getPreamble();
    }

    public DaxFrame parseFrame(String msg) {
        return parse(msg, WorkMode.FULL_FRAME);
    }

    private DaxFrame parse(String frameStr, WorkMode workMode) {
        if (frameStr == null || frameStr.isEmpty()) {
            throw new DaxFrameParserException("Frame is EMPTY!");
        }

        int openingBrace = frameStr.indexOf(DaxCoreConstants.PREAMBLE_OPENING_BRACE);
        if (openingBrace < 0
                || !frameStr.substring(0, openingBrace).trim().equals(DaxCoreConstants.DAXP_SYMBOL)) {
            throw new DaxFrameParserException("Invalid DAXP message header or missing preamble!");
        }
        int closingBrace = frameStr.indexOf(DaxCoreConstants.PREAMBLE_CLOSING_BRACE, openingBrace + 1);
        if (closingBrace < 0) {
            throw new DaxFrameParserException("Missing closing preamble brace!");
        }
        DaxPreamble preamble = preambleParser.parse(frameStr.substring(openingBrace + 1, closingBrace));
        char pairSeparator = preamble.getPairSeparator();

        DaxFrame frame = new DaxFrame();
        frame.setPreamble(preamble);
        if (workMode == WorkMode.PREAMBLE_ONLY) {
            return frame;
        }
        List<DaxMessage> messageList = new ArrayList<>();
        List<DaxPair<?>> listOfPair = new ArrayList<>();

        int sum = 0;
        int prevIdx = closingBrace + 1;
        boolean isChecksumLast = true;

        int nextIdx;
//        while ((nextIdx = frameStr.indexOf(pairSeparator, prevIdx)) != -1) {
        while ((nextIdx = indexOfUnescaped(
                        frameStr, pairSeparator, prevIdx)) !=-1)
        {
            // Locate Operator
            int operatorIdx = -1;
            char foundOperator = 0;
            for (int i = prevIdx; i < nextIdx; i++) {
                char c = frameStr.charAt(i);
                if (c == OPERATOR_EQUAL || c == OPERATOR_REFERENCE || c == OPERATOR_ACTION) {
                    operatorIdx = i;
                    foundOperator = c;
                    break;
                }
            }

            if (operatorIdx == -1) {
                throw new DaxFrameParserException("Incompatible or missing operator in DAXP message tag!");
            }

            String tagStr = frameStr.substring(prevIdx, operatorIdx).trim();
            String valueStr = frameStr.substring(operatorIdx + 1, nextIdx);

            try {
                DaxTag tag = tagParser.parseDaxTag(tagStr, preamble.getNamespaceId());
                if (isChecksumLast && !tag.equals(DaxCoreTags.MSG_TYPE)) {
                    throw new DaxPreambleException("First tag in message is not MSG_TYPE");
                }
                if (!isChecksumLast && tag.equals(DaxCoreTags.MSG_TYPE)) {
                    throw new DaxFrameParserException("Message is missing CHECKSUM before the next MSG_TYPE!");
                }
                isChecksumLast = false;

                if (tag.equals(DaxCoreTags.MSG_TYPE)) {
                    listOfPair.clear();
                }

                DaxPair<?> pair = pairCodec.decode(
                        tag,
                        unescapeValue(valueStr, pairSeparator),
                        foundOperator,
                        preamble.getNamespaceId());

                listOfPair.add(pair);

                if (tag.equals(DaxCoreTags.CHECKSUM)) {
                    int expectedChecksum = DaxChecksumService.calculateModulo(sum);
                    int actualChecksum;
                    try {
                        actualChecksum = Integer.parseInt(valueStr);
                    } catch (NumberFormatException e) {
                        throw new DaxFrameParserException("Invalid Checksum format: " + valueStr, e);
                    }

                    if (expectedChecksum != actualChecksum) {
                        logger.error("BAD CHECKSUM {}. Expected checksum is {}.", actualChecksum, expectedChecksum);
                        throw new DaxFrameParserException("Checksum validation failed!");
                    }

                    messageList.add(messageFactory.createMsg(new ArrayList<>(listOfPair)));
                    listOfPair.clear();
                    sum = 0;
                    isChecksumLast = true;
                } else {
                    sum += DaxChecksumService.calculateSum(tagStr);
                    sum += DaxChecksumService.calculateSum(valueStr);
                }
            } catch (DaxFrameParserException e) {
                throw e;
            } catch (Exception e) {
                throw new DaxFrameParserException("Error parsing frame body", e);
            }

            prevIdx = nextIdx + 1;
        }

        if (prevIdx < frameStr.length()) {
            throw new DaxFrameParserException("Unterminated message field after preamble!");
        }
        validateAndFinalizeFrame(frame, preamble, messageList, isChecksumLast);
        return frame;
    }

    private void validateAndFinalizeFrame(DaxFrame frame, DaxPreamble preamble, List<DaxMessage> messageList, boolean isChecksumLast) {
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

    private int indexOfUnescaped(
            String text, char separator, int fromIndex) {

        if (separator == ESCAPE_CHAR) {
            throw new IllegalArgumentException(
                    "Separator cannot equal the escape character");
        }

        int position = text.indexOf(separator, fromIndex);

        while (position >= 0) {
            int backslashes = 0;

            for (int i = position - 1;
                    i >= 0 && text.charAt(i) == ESCAPE_CHAR;
                    i--) {
                backslashes++;
            }

            if (backslashes % 2 == 0) {
                return position;
            }

            position = text.indexOf(separator, position + 1);
        }

        return -1;
    }

    private String unescapeValue(String value, char separator) {
        StringBuilder result = new StringBuilder(value.length());

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == ESCAPE_CHAR && i + 1 < value.length()) {
                char next = value.charAt(i + 1);
                if (next == separator || next == ESCAPE_CHAR) {
                    result.append(next);
                    i++;
                    continue;
                }
            }
            result.append(c);
        }

        return result.toString();
    }

}
