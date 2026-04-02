package org.daxprotocol.core.ut.Dax_00_10_Parser;

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.ut.Dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxParserService_tagTestBaseTest extends DaxConfigBaseTest {

    @Test
    void parseTag10(){
        String tagStr = "9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxConfig.DAXP_CONTEXT_ID,9);
        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag20(){
        String tagStr = " 8";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxConfig.DAXP_CONTEXT_ID,8);
        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag30(){
        String tagStr = " 8 ";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxConfig.DAXP_CONTEXT_ID,8);
        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag40(){
        String tagStr = "$:9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxConfig.DAXP_CONTEXT_ID,9);
        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag50(){
        String tagStr = "$ : 9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxConfig.DAXP_CONTEXT_ID,9);

        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag51(){
        String tagStr = "$\n : 9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxConfig.DAXP_CONTEXT_ID,9);

        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag52(){
        String tagStr = "X\n : 9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxConfig.DAXP_CONTEXT_ID,9);

        Assertions.assertNotEquals(expectedTag ,tag);
    }

    @Test
    void parseTag60(){
        String tagStr = " : 7";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = new DaxTag(DaxConfig.DAXP_CONTEXT_ID,7);

        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag70(){
        String tagStr = " w 9";
        Assertions.assertThrows(RuntimeException.class, () ->
                parser.parseDaxTag(tagStr, appContextId));  // TODO change to dedicated exception
    }

    @Test
    void parseTag80(){
        String tagStr = "FIX:9";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        int expContextId = provider.getContextMapper().getReferenceId("FIX");
        DaxTag expectedTag  = new DaxTag(expContextId,9);

        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag90(){
        String tagStr = "CRM:1029";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        int expContextId = provider.getContextMapper().getReferenceId("CRM");
        DaxTag expectedTag  = new DaxTag(expContextId,1029);

        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag100(){
        String tagStr = "$CRM:1029";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        int expContextId = provider.getContextMapper().getReferenceId("CRM");
        DaxTag expectedTag  = new DaxTag(expContextId,1029);

        //Assertions.assertEquals(expectedTag ,tag);
        Assertions.assertNotEquals(tag.getContextId(),appContextId);

    }

    @Test
    void parseTag101(){
        String tagStr = "$CRM$:1029";
        DaxTag tag = parser.parseDaxTag(tagStr, appContextId);
        int expContextId = provider.getContextMapper().getReferenceId("CRM");
        DaxTag expectedTag  = new DaxTag(expContextId,1029);

        //Assertions.assertEquals(expectedTag ,tag);
        Assertions.assertNotEquals(tag.getContextId(),appContextId);

    }


}
