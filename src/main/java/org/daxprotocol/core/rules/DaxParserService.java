package org.daxprotocol.core.rules;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.mapper.DaxReferenceMapper;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DaxParserService {

    DaxpConfig config;
    DaxReferenceMapper mapper;
    Pattern ctxTagPattern;

    public DaxParserService(DaxpConfig config, DaxReferenceMapper mapper){
        this.config = config;
        this.mapper = mapper;
        this.ctxTagPattern = DaxPatternFactory.compileContextTagPattern(config);
    }

    //TODO rebuild and  DaxProtocolRules
    public DaxTag parseDaxTag(String tagStr) {
        int tagId;
        int contextId = 0;

        Matcher m = ctxTagPattern.matcher(tagStr);

        if (m.matches()) {
            String contextSymbol = m.group(1); // null if no context
            tagId = Integer.parseInt(m.group(2));

            if (contextSymbol == null){
//             if (tagId > DaxpConfig.MAX_DAXP_TAG_ID) {
                contextId = config.getAppContextId();
                //           }
            }
            else {
                contextId = mapper.getReferenceId(contextSymbol);
            }
            return new DaxTag(contextId, tagId);
        }
        throw new RuntimeException("NOT correct DaxTag "+tagStr);
    }

    public List<DaxTag> parseDaxTagList (String tagListStr){

        return Arrays.stream(tagListStr.split(String.valueOf(DaxpConfig.TAG_LIST_SEPARATOR)))
                .map(String::trim)
                .map(this::parseDaxTag)
                .collect(Collectors.toList());
    }


//    public static List<DaxStringPair> parsePairs(String msg, Pattern pairPattern, String dftContext) {
//        List<DaxStringPair> list = new ArrayList<>();
//        Matcher m = pairPattern.matcher(msg);
//        while (m.find()) {
//            String contextSymbol;
//            String contextStr = m.group(1);
//            int tagId = Integer.parseInt(m.group(2));
//            int contextId;
//            if (contextStr == null) {
//                if (tagId < DaxpConfig.MAX_DAXP_TAG_ID) {
//                    contextSymbol = DaxpConfig.DAX_CONTEXT_SYMBOL;
//                } else {
//                    contextSymbol = dftContext;
//                }
//            } else {
//                contextSymbol = contextStr;
//            }
//            contextId = DaxContextMapper.getContextId(contextSymbol);
//            list.add(new DaxStringPair(new DaxTag(contextId, tagId), m.group(3)));
//        }
//        return list;
//    }


    //    public static Map<String, String> parseMap(String msgPart, Pattern pairPattern  ) {
//        Map<String, String> map = new HashMap<>();
//
//        Matcher m = pairPattern.matcher(msgPart);
//
//        while (m.find()) {
//            map.put(m.group(1), m.group(2));
//        }
//        return map;
//    }


}
