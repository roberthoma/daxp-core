package org.daxprotocol.core.parser;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextMapper;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DaxParserService {

    DaxpConfig config;
    DaxContextMapper mapper;
    public DaxParserService(DaxpConfig config, DaxContextMapper mapper){
        this.config = config;
        this.mapper = mapper;
    }



    public DaxTag parseDaxTag(int appContextId, String tagStr) {
        int tagId;
        int contextId = 0;

        //todo move to consts paterns
        Pattern pattern = Pattern.compile("^(?:([A-Za-z]+)"+ DaxpConfig.CONTEXT_TAG_SEPARATOR+")?([0-9]+)$");

        Matcher m = pattern.matcher(tagStr);

        if (m.matches()) {
            String contextSymbol = m.group(1); // null if no context
            tagId = Integer.parseInt(m.group(2));

            if (contextSymbol == null){
//             if (tagId > DaxpConfig.MAX_DAXP_TAG_ID) {
                contextId = appContextId;
                //           }
            }
            else {
                contextId = mapper.getContextId(contextSymbol);
            }


            return new DaxTag(contextId, tagId);
        }



        throw new RuntimeException("NOT correct DaxTag "+tagStr);
    }
}
