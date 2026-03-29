package org.daxprotocol.core.rules;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DaxParserService {

    DaxpConfig config;
    DaxStringReferenceMapper contextMapper;
    Pattern ctxTagPattern;

    public DaxParserService(DaxpConfig config, DaxStringReferenceMapper contextMapper){
        this.config = config;
        this.contextMapper = contextMapper;
        this.ctxTagPattern = DaxPatternFactory.compileContextTagPattern(config);
    }

    //TODO rebuild and  DaxProtocolRules
    //TODO Add context from preamble , or add contextId

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
                contextId = contextMapper.getReferenceId(contextSymbol);
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


    //TODO rebuild and  DaxProtocolRules


    //TODO change to List<DaxPair<?>>
    public List<DaxStringPair> parsePairs(String msg, Pattern pairPattern, int  msgContextId) {
        List<DaxStringPair> list = new ArrayList<>();
        Matcher matcher = pairPattern.matcher(msg);

        while (matcher.find()) {

            String contextStr = matcher.group(1);
            int tagId = Integer.parseInt(matcher.group(2));
            //-------------
            int contextId;
            if (contextStr == null) {
                if (tagId < DaxpConfig.DAXP_MAX_TAG_ID) {
                    contextId = DaxpConfig.DAXP_CONTEXT_ID;
                } else {
                    contextId = msgContextId;
                }
            } else {
                contextId = contextMapper.getReferenceId(contextStr);
            }
            //-------------

            list.add(new DaxStringPair(new DaxTag(contextId, tagId), matcher.group(3)));
        }
        return list;
    }


}
