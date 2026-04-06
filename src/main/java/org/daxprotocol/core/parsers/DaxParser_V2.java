package org.daxprotocol.core.parsers;

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.*;
import java.util.stream.Collectors;

public class DaxParser_V2 implements DaxParser {

    DaxConfig config;
    DaxStringReferenceMapper contextMapper;
    DaxParserTools parserTools =  DaxParserTools.getInstance();
    DaxParserTag parserTag ;
    DaxDictionary daxDic;
    DaxParserMessage parserMessage;
    DaxMessageCodec messageCodec;

    public DaxParser_V2(DaxConfig config, DaxContextMapper contextMapper , DaxDictionary daxDic,
            DaxMessageCodec messageCodec)
     {
        this.config = config;
        this.contextMapper = contextMapper;
        this.parserTag = new DaxParserTag(contextMapper);
        this.daxDic = daxDic;
        this.messageCodec = messageCodec;
         parserMessage = new DaxParserMessage( config,contextMapper, parserTag, daxDic, messageCodec); //,  messageFactory);

    }



//    @Override public DaxPair<?> parsePair(String pairStr, int msgContextId) {
//        return null;
//    }

    /**
     * Parses a message string into a list of DaxPair objects using high-performance
     * manual index tracking instead of Regular Expressions or String.split().
     * * This method is designed to be "GC-friendly" by avoiding unnecessary
     * String allocations. it handles:
     * <ul>
     * <li>Leading/trailing whitespaces and newlines (trimming)</li>
     * <li>Optional contexts separated by a colon (e.g., "CTX:100=val")</li>
     * <li>Leading or trailing separators</li>
     * <li>Empty segments (e.g., "||")</li>
     * </ul>
     *
  //   * @param msg     The raw input string to parse.
     * @param msgContextId The default context ID to use if no context prefix is found
     * and the tag is above the reserved range.
     * @return A list of parsed DaxPair objects.
     */
/*
    @Override
    public List<DaxPair<?>> parsePairs(String msg,  int msgContextId) {

        List<DaxPair<?>> list = new ArrayList<>();
        if (msg == null || msg.isEmpty()) return list;

        char equalSign = DaxConfig.EQUAL;
        char sep = DaxConfig.PAIR_SEPARATOR;

        int len = msg.length();
        int cursor = 0;

        while (cursor < len) {
            // 1. Locate the end of the current pair (the separator)
            int nextSep = msg.indexOf(sep, cursor);
            int endOfPair = (nextSep == -1) ? len : nextSep;

            // --- Handle Whitespace: Find the actual start and end of the pair ---
            int pairStart = cursor;
            while (pairStart < endOfPair && msg.charAt(pairStart) <= ' ') {
                pairStart++; // Skip leading spaces/tabs/newlines
            }

            int pairEnd = endOfPair;
            while (pairEnd > pairStart && msg.charAt(pairEnd - 1) <= ' ') {
                pairEnd--; // Skip trailing spaces before the separator
            }

            // Process only if the segment is not empty
            if (pairEnd > pairStart) {
                // 2. Locate the assignment character (=)
                int eqPos = msg.indexOf(equalSign, pairStart);

                // Ensure '=' exists and belongs to the current segment
                if (eqPos != -1 && eqPos < pairEnd) {

                    // --- Handle whitespace around the '=' sign ---
                    int keyEnd = eqPos;
                    while (keyEnd > pairStart && msg.charAt(keyEnd - 1) <= ' ') {
                        keyEnd--; // Trim key: "100 =" -> "100"
                    }

                    int valStart = eqPos + 1;
                    while (valStart < pairEnd && msg.charAt(valStart) <= ' ') {
                        valStart++; // Trim value: "= 2000" -> "2000"
                    }

                    // 3. Identify Key components (Optional Context : TagId)
                    int colonPos = -1;
                    for (int i = pairStart; i < keyEnd; i++) {
                        if (msg.charAt(i) == DaxConfig.CONTEXT_TAG_SEPARATOR_CHAR) {
                            colonPos = i;
                            break;
                        }
                    }

                    String contextStr = null;
                    int tagId;

                    try {
                        if (colonPos != -1) {
                            // Extract Context String (e.g., "FIX")
                            int ctxEnd = colonPos;
                            while (ctxEnd > pairStart && msg.charAt(ctxEnd - 1) <= ' ') ctxEnd--;
                            contextStr = msg.substring(pairStart, ctxEnd);

                            // Extract Tag ID (after the colon)
                            int tagStart = colonPos + 1;
                            while (tagStart < keyEnd && msg.charAt(tagStart) <= ' ') tagStart++;
                            tagId = parserTools.parseIntFromSequence(msg, tagStart, keyEnd);
                        } else {
                            // No context prefix, parse the whole key as Tag ID
                            tagId = parserTools.parseIntFromSequence(msg, pairStart, keyEnd);
                        }

                        // 4. Extract Value (substring from the already trimmed indices)
                        String value = msg.substring(valStart, pairEnd);

                        // 5. Determine the Context ID based on business logic
                        int contextId;
                        if (contextStr == null || contextStr.isEmpty()) {
                            // Default logic: small tags use a specific context, others use msgContextId
                            contextId = (tagId < DaxConfig.DAXP_MAX_TAG_ID)
                                    ? DaxConfig.DAXP_CONTEXT_ID
                                    : msgContextId;
                        } else {
                            // Map the context string (e.g., "FIX") to its numeric reference
                            contextId = contextMapper.getReferenceId(contextStr);
                        }

                        list.add(new DaxPair<>(new DaxTag(contextId, tagId), value));

                    } catch (NumberFormatException e) {
                        // Silently ignore malformed Tag IDs to match original Regex behavior
                    }
                }
            }

            // Break if we've reached the end of the string, otherwise move past the separator
            if (nextSep == -1) break;
            cursor = nextSep + 1;
        }
        return list;
    }
*/
    @Override
    public DaxTag parseDaxTag(String tagStr, int msgContextId) {
        return parserTag.parseDaxTag(tagStr,  msgContextId);
    }
    //------------------------------------------------------
/*
    public Map<DaxTag, String> parserBlock(String blockStr) {
        Map<DaxTag, String> result = new HashMap<>();

        if (blockStr == null || blockStr.isEmpty()) {
            return result;
        }

        int length = blockStr.length();
        int pos = 0;

        while (pos < length) {
            if (blockStr.charAt(pos) == DaxConfig.PAIR_SEPARATOR) {
                pos++;
                continue;
            }

            int nextSep = blockStr.indexOf(DaxConfig.PAIR_SEPARATOR, pos);
            if (nextSep == -1) {
                nextSep = length;
            }

            if (nextSep <= pos) {
                pos = nextSep + 1;
                continue;
            }

            int eqPos = blockStr.indexOf(DaxConfig.EQUAL, pos);
            if (eqPos == -1 || eqPos >= nextSep) {
                throw new IllegalArgumentException("Invalid pair: " + blockStr.substring(pos, nextSep));
            }

            String cx = "";
            String tagText;

            int colonPos = blockStr.indexOf(DaxConfig.CONTEXT_TAG_SEPARATOR_CHAR, pos);
            if (colonPos != -1 && colonPos < eqPos) {
                cx = blockStr.substring(pos, colonPos);
                tagText = blockStr.substring(colonPos + 1, eqPos);
            } else {
                tagText = blockStr.substring(pos, eqPos);
            }

            int tagId = Integer.parseInt(tagText);
            String value = blockStr.substring(eqPos + 1, nextSep);

            //TODO Add to method
            int contextId = cx.isBlank() ?
                    config.getAppContextId():
                    contextMapper.getReferenceId(cx);

            DaxTag tag =  new DaxTag(contextId ,tagId);

            result.put(tag, value);
            pos = nextSep + 1;
        }

        return result;
    }
*/

    /**
     * Splits the input text into a list of strings based on a specific tag delimiter.
     * This approach is more memory-efficient than Regex-based split() for large datasets.
     *
//     * @param text The raw protocol data to be parsed.
//     * @param tag  The delimiter tag (e.g., "|7=").
     * @return A list of segments separated by the specified tag.
     */
/*
    public  List<String> splitByTag(String text, String tag) {
        List<String> result = new ArrayList<>();

        // Safety check for null or empty input
        if (text == null || text.isEmpty()) {
            return result;
        }

        int start = 0;
        int nextPos;

        // Iterate through the text searching for the tag's occurrence
        while ((nextPos = text.indexOf(tag, start)) != -1) {

            // If there is content before the first occurrence of the tag (e.g., a header),
            // we capture it as the first element.
            if (nextPos > start) {
                String prefix = text.substring(start, nextPos).trim();
                if (!prefix.isEmpty()) {
                    result.add(prefix);
                }
            }

            // Update the starting point to the current tag's position
            start = nextPos;

            // Look for the position of the NEXT tag to determine the end of the current segment
            int endOfCurrent = text.indexOf(tag, start + tag.length());

            if (endOfCurrent == -1) {
                // No more tags found; capture the remaining text from 'start' to the end of the string
                String lastSegment = text.substring(start).trim();
                if (!lastSegment.isEmpty()) {
                    result.add(lastSegment);
                }
                // Move start pointer to the end to terminate the loop
                start = text.length();
                break;
            } else {
                // Extract the segment starting from the current tag up to (but not including) the next tag
                String segment = text.substring(start, endOfCurrent).trim();
                if (!segment.isEmpty()) {
                    result.add(segment);
                }
                // Move the pointer to the beginning of the next tag for the next iteration
                start = endOfCurrent;
            }
        }

        return result;
    }

    public List<List<DaxPair<?>>> splitMessages(List<DaxPair<?>> allPairs) {
        List<List<DaxPair<?>>> result = new ArrayList<>();
        List<DaxPair<?>> current = new ArrayList<>();

        for (DaxPair<?> pair : allPairs) {
            if (pair.getTag().equals(DaxTagConst.MSG_TYPE)) {
                // start of a new message
                if (!current.isEmpty()) {
                    // if previous message wasn't closed correctly, save it anyway
                    result.add(new ArrayList<>(current));
                    current.clear();
                }
            }

            current.add(pair);

            if (pair.getTag().equals(DaxTagConst.CHECKSUM)) {
                // end of current message
                result.add(new ArrayList<>(current));
                current.clear();
            }
        }

        // handle last incomplete message (optional)
        if (!current.isEmpty()) {
            result.add(current);
        }

        return result;
    }
    */

    //------------------------
    //TODO refactor
    @Override
    public List<DaxTag> parseDaxTagList (String tagListStr, int msgContextId){

        return Arrays.stream(tagListStr.split(String.valueOf(DaxConfig.TAG_LIST_SEPARATOR)))
                .map(String::trim)
                .map(s ->  parseDaxTag(s,msgContextId))
                .collect(Collectors.toList());
    }
    /*
    public List<DaxTag> parseDaxTagList(String tagListStr, int msgContextId) {
        List<DaxTag> result = new ArrayList<>();
        if (tagListStr == null || tagListStr.isEmpty()) {
            return result;
        }

        int start = 0;
        int end = tagListStr.indexOf(DaxConfig.TAG_LIST_SEPARATOR_CHAR);

        while (end != -1) {
            String tagStr = tagListStr.substring(start, end).trim();
            result.add(parseDaxTag(tagStr, msgContextId));

            start = end + 1;
            end = tagListStr.indexOf(DaxConfig.TAG_LIST_SEPARATOR_CHAR, start);
        }

        // Dodanie ostatniego elementu (za ostatnim separatorem)
        String lastTagStr = tagListStr.substring(start).trim();
        result.add(parseDaxTag(lastTagStr, msgContextId));

        return result;
    }
*/
    @Override public DaxPreamble parsePreamble(String msg) {
        return parserMessage.parsePreamble(msg);
    }

    @Override
    public List<DaxMessage> parseMessageList(String msg){
        return parserMessage.parseMessageList(msg);
    }

    @Override public DaxPreamble decodePreambleFromMap(Map<String, String> params) {
        return null;
    }

    @Override public List<DaxMessage> decodeMessageFromMap(Map<String, String> params) {
        return List.of();
    }

}
