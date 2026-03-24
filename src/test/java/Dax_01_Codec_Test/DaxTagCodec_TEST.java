package Dax_01_Codec_Test;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxTagCodec_TEST extends DaxTestConfig {

    @Test
    public void encode_test1(){
        DaxTagCodec codec =  crmProvider.getTagCodec();
        DaxTag tag = new DaxTag(crmProvider.getConfig().getAppContextId(),123);
        Assertions.assertEquals("CRM:123",codec.encode(tag));
    }

    @Test
    public void decode_test1(){
        DaxTagCodec codec =  crmProvider.getTagCodec();

        DaxTag tag = new DaxTag(crmProvider.getConfig().getAppContextId(),345);

        //cmrProvider.getP

        Assertions.assertEquals(tag.getTagId(),codec.decode("345").getTagId());
    }


}
