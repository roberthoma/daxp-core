package org.daxprotocol.core.ut.Dax_00_10_Parser;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxBaseTestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxParserService_tagTest extends DaxBaseTestConfig {

    @Test
    void parseTag10(){
        String tagStr = "9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,9);
        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag20(){
        String tagStr = " 8";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,8);
        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag30(){
        String tagStr = " 8 ";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,8);
        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag40(){
        String tagStr = "$:9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,9);

        System.out.println("Expected ctx = "+expectedTag.getContextId());
        System.out.println("Expected  id = "+expectedTag.getTagId());
        System.out.println("Actual   ctx = "+tag.getContextId());
        System.out.println("Actual    id = "+tag.getTagId());

        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag50(){
        String tagStr = "$ : 9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,9);

        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag60(){
        String tagStr = " : 9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,9);

        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag70(){
        String tagStr = " w 9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxpConfig.DAXP_CONTEXT_ID,9);

        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag80(){
        String tagStr = "FIX:9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        int expContextId = provider.getContextMapper().getReferenceId("FIX");
        DaxTag expectedTag  = new DaxTag(expContextId,9);

        Assertions.assertEquals(expectedTag ,tag);
    }

}
