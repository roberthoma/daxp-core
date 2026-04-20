package org.daxprotocol.core.unit_test.dax_00_20_base_parser;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.exceptions.DaxTagParserException;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxTagParserTestBaseTest extends DaxConfigBaseTest {

    @Test
    void parseTag10(){
        String tagStr = "$:1";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        Assertions.assertEquals(DaxCoreTags.MSG_TYPE ,tag);
    }
    @Test
    void parseTag11(){
        String tagStr = "1";
        DaxTag tag = tagParser.parseDaxTag(tagStr, DaxCoreConstants.DAXP_CONTEXT_ID);
        Assertions.assertEquals(DaxCoreTags.MSG_TYPE ,tag);
    }
    @Test
    void parseTag20(){
        String tagStr = " 8";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = DaxTag.of(appContextId,8);
        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag30(){
        String tagStr = " 2000 ";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = DaxTag.of(appContextId,2000);
        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag40(){
        String tagStr = "$:"+DaxCoreTags.CHECKSUM.getTagId();
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        Assertions.assertEquals(DaxCoreTags.CHECKSUM ,tag);
    }

    @Test
    void parseTag45(){
        String tagStr = "$\n:\n\n"+DaxCoreTags.CHECKSUM.getTagId();
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        Assertions.assertEquals(DaxCoreTags.CHECKSUM ,tag);
    }

    @Test
    void parseTag51(){
        String tagStr = "$\n : "+DaxCoreTags.MSG_TYPE.getTagId();;
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        Assertions.assertEquals(DaxCoreTags.MSG_TYPE ,tag);
    }

    @Test
    void parseTag52(){
        String tagStr = "X\n : "+DaxCoreTags.MSG_TYPE.getTagId();
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        Assertions.assertNotEquals(DaxCoreTags.MSG_TYPE ,tag);
    }

    @Test
    void parseTag60(){
        String tagStr = " : 1029";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        DaxTag expectedTag  = DaxTag.of(appContextId,1029);
        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag70(){
        String tagStr = " w 9";
        Assertions.assertThrows(DaxTagParserException.class, () ->
                tagParser.parseDaxTag(tagStr, appContextId));
    }

    @Test
    void parseTag80(){
        String tagStr = "FIX:9";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        int expContextId = daxEngine.getContextMapper().getReferenceId("FIX");
        DaxTag expectedTag  = DaxTag.of(expContextId,9);

        Assertions.assertEquals(expectedTag ,tag);
    }
    @Test
    void parseTag90(){
        String tagStr = "CRM:1029";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        int expContextId = daxEngine.getContextMapper().getReferenceId("CRM");
        DaxTag expectedTag  = DaxTag.of(expContextId,1029);

        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag100(){
        String tagStr = "$CRM:1029";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        int expContextId = daxEngine.getContextMapper().getReferenceId("CRM");
        DaxTag expectedTag  = DaxTag.of(expContextId,1029);

        //Assertions.assertEquals(expectedTag ,tag);
        Assertions.assertNotEquals(appContextId,tag.getContextId());
        Assertions.assertNotEquals(DaxCoreConstants.DAXP_CONTEXT_ID,tag.getContextId());

    }

    @Test
    void parseTag101(){
        String tagStr = "$CRM$:1029";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        int expContextId = daxEngine.getContextMapper().getReferenceId("CRM");
        DaxTag expectedTag  = DaxTag.of(expContextId,1029);

        Assertions.assertNotEquals(expectedTag ,tag);
        Assertions.assertNotEquals(appContextId,tag.getContextId());
        Assertions.assertNotEquals(DaxCoreConstants.DAXP_CONTEXT_ID,tag.getContextId());

    }   @Test
    void parseTag102(){
        String tagStr = "$CRM$:1029";
        DaxTag tag = tagParser.parseDaxTag(tagStr, appContextId);
        int expContextId = daxEngine.getContextMapper().getReferenceId("$CRM$");
        DaxTag expectedTag  = DaxTag.of(expContextId,1029);

        Assertions.assertEquals(expectedTag ,tag);
    }

    @Test
    void parseTag110(){
        String tagStr = "XYZ:1029";
        DaxTag tag = tagParser.parseDaxTag(tagStr, 4); //ContextId = 4 is only for test lower 100
        int expContextId = daxEngine.getConfig().getAppContextId();
        DaxTag expectedTag  = DaxTag.of(expContextId,1029);

        Assertions.assertEquals(expectedTag ,tag);
        Assertions.assertEquals(appContextId,tag.getContextId());

    }
    @Test
    void parseTag120(){
        String abcCtx = "ABC";
        int abcContextId = contextMapper.getReferenceId(abcCtx);
        String tagStr = "1029";
        DaxTag tag = tagParser.parseDaxTag(tagStr, abcContextId);
        DaxTag expectedTag  = DaxTag.of(abcContextId,1029);
        Assertions.assertEquals(expectedTag ,tag);
        Assertions.assertEquals(abcContextId,tag.getContextId());
    }
}
