package Dax_01_Codec_Test;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static Dax_00_Base_test.DaxTestConfig.cmrProvider;

public class DaxTagCodec_TEST extends DaxTestConfig {

    @Test
    public void encode_test1(){
        DaxTagCodec codec =  cmrProvider.getTagCodec();
        DaxTag tag = new DaxTag(1,123);
        Assertions.assertEquals("123",codec.encode(tag));
    }

    @Test
    public void decode_test1(){
        DaxTagCodec codec =  cmrProvider.getTagCodec();

        DaxTag tag = new DaxTag(1,345);

        //cmrProvider.getP

        Assertions.assertEquals(tag.getTagId(),codec.decode("345").getTagId());
    }


}
