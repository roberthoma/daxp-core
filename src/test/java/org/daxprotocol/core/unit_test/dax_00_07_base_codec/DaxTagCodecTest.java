package org.daxprotocol.core.unit_test.dax_00_07_base_codec;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.unit_test.dax_00_00_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxTagCodecTest extends DaxConfigBaseTest {

    @Test
    void tagCodec_1(){
        DaxTag tag = DaxCoreTags.MSG_TYPE;
        String tagStr = "$:"+ DaxCoreTags.MSG_TYPE.getTagId();
        Assertions.assertEquals(tagStr, tagCodec.encode(tag));
    }
    @Test
    void tagCodec_2(){
        int contextId = contextMapper.getReferenceId("FIX");
        DaxTag tag = DaxTag.of(contextId, 45 );
        String tagStr = "FIX:45";
        Assertions.assertEquals(tagStr, tagCodec.encode(tag));
    }

}
