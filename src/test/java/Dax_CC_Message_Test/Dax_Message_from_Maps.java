package Dax_CC_Message_Test;

import Dax_00_Base_test.ContextConst;
import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.mapper.DaxReferenceMapper;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxStringPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Dax_Message_from_Maps extends DaxTestConfig {



    @Test
    public void testFactoryMsgFromMap(){
        DaxMessageFactory factory = cmrProvider.getMessageFactory();
        DaxMessageCodec codec = cmrProvider.getMessageCodec();
        DaxReferenceMapper contextMapper = cmrProvider.getContextMapper();
        int appContextId = cmrProvider.getConfig().getAppContextId();
        Map<DaxTag,DaxPair<?>> pairMap = new HashMap<>();
        int fixContextId = contextMapper.getReferenceId(ContextConst.CTX_FIX_PROTOCOL);

        DaxTag tag1 = new DaxTag(fixContextId, 123);
        DaxStringPair pai1 = new DaxStringPair(tag1,"value123");

        DaxTag tag2 = new DaxTag(fixContextId, 345);
        DaxStringPair pai2 = new DaxStringPair(tag2,"value456");

        DaxTag tag3 = new DaxTag(appContextId, 2456);
        DaxStringPair pai3 = new DaxStringPair(tag3,"appValue222");

        pairMap.put(tag1,pai1);
        pairMap.put(tag2,pai2);
        pairMap.put(tag3,pai3);

        DaxMessage msg = factory.toDaxMessageFromPairMap("FXM",pairMap);


        System.out.println(codec.encode(msg));



    }
}
