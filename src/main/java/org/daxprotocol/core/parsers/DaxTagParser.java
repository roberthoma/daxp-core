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

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.exceptions.DaxTagParserException;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxTagParser {

    DaxContextMapper contextMapper;

    public DaxTagParser(DaxContextMapper contextMapper) {
        this.contextMapper = contextMapper;
    }


    /**
     * Efficiently parses an integer from a specific range within a String
     * while ignoring any trailing whitespaces within that range.
     */
    public int parseIntFromSequence(String seq, int start, int end) {
        if (start >= end) {
            throw new DaxTagParserException("Empty tag ID");
        }

        int num = 0;
        boolean hasDigits = false;

        for (int i = start; i < end; i++) {
            char c = seq.charAt(i);
            if (c >= '0' && c <= '9') {
                num = num * 10 + (c - '0');
                hasDigits = true;
            } else if (c <= ' ') {
                // If we found a space after digits, ensure no more digits follow
                // (Standard trim-like behavior)
                continue;
            } else {
                throw new DaxTagParserException("Invalid character: " + c);
            }
        }

        if (!hasDigits)
            throw new DaxTagParserException("No digits found");
        return num;
    }


    /**
     * Parses a string representation of a DaxTag (e.g., "  CTX:100  ")
     * into a DaxTag object using high-performance index tracking.
     * * This version handles:
     * - Leading/trailing whitespaces around the whole string.
     * - Whitespaces around the separator (e.g., "CTX : 100").
     * - Single-character separator defined in config.
     *
     * @param tagStr The raw tag string to parse.
     * @return A new DaxTag object with mapped contextId and tagId.
     * @throws DaxTagParserException if the format is invalid or tagId is not a numerical value.
     */

    public DaxTag parseDaxTag(String tagStr, int msgContextId) {

        if (tagStr == null) {
            throw new DaxTagParserException("NOT correct DaxTag: Input is null");
        }

        // 1. Trim the entire string without creating a new String object
        int start = 0;
        int end = tagStr.length();
        while (start < end && tagStr.charAt(start) <= ' ') start++;
        while (end > start && tagStr.charAt(end - 1) <= ' ') end--;

        if (start >= end) {
            throw new DaxTagParserException("NOT correct DaxTag: Input is empty or only whitespace");
        }

        char separator = DaxConfig.CONTEXT_TAG_SEPARATOR_CHAR;
        int separatorPos = -1;

        // 2. Search for the separator only within the trimmed range
        for (int i = start; i < end; i++) {
            if (tagStr.charAt(i) == separator) {
                separatorPos = i;
                break;
            }
        }

        String contextSymbol = null;
        int tagIdStart;

        if (separatorPos != -1) {
            // --- Context Prefix Found ---
            // Trim the context symbol (handle "CTX :")
            int ctxEnd = separatorPos;
            while (ctxEnd > start && tagStr.charAt(ctxEnd - 1) <= ' ') {
                ctxEnd--;
            }

            if (ctxEnd > start) {
                contextSymbol = tagStr.substring(start, ctxEnd);
            }

            // Tag ID starts after the separator
            tagIdStart = separatorPos + 1;
        } else {
            // --- No Context Prefix ---
            tagIdStart = start;
        }

        // 3. Trim leading spaces for Tag ID (handle ": 100")
        while (tagIdStart < end && tagStr.charAt(tagIdStart) <= ' ') {
            tagIdStart++;
        }

        // 4. Parse Tag ID directly from the sequence
        int tagId;
        try {
            tagId = parseIntFromSequence(tagStr, tagIdStart, end);
        } catch (NumberFormatException e) {
            throw new DaxTagParserException("NOT correct DaxTag: " + tagStr, e);
        }

        // 5. Context ID resolution logic
        int contextId ;
        if (contextSymbol == null || contextSymbol.isEmpty()) {
            if( tagId <= DaxConfig.DAXP_MAX_TAG_ID) {
                contextId = DaxConfig.DAXP_CONTEXT_ID;
            }
            else {
                contextId = msgContextId ;// config.getAppContextId();
            }
        } else {
            contextId = contextMapper.getReferenceId(contextSymbol);
        }
        if(contextId==DaxConfig.DAXP_CONTEXT_ID
                && tagId > DaxConfig.DAXP_MAX_TAG_ID)
        {
            throw new DaxTagParserException(" Tag "+tagId+" can't be in DAXP context "+
                                              DaxConfig.DAXP_CONTEXT_TAG_PREFIX  +" !!! ");
        }

        //6. Is ok return new DaxTag
        return new DaxTag(contextId, tagId);
    }

}
