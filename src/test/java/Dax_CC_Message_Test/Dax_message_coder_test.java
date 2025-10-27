package Dax_CC_Message_Test;

import Dax_00_Base_test.CustomerDaxDic;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.DaxMessageFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dax_message_coder_test {

    @Test
    void testRequest() {
        DaxMessageFactory req = new DaxMessageFactory();
        DaxMessageCodec codec = new DaxMessageCodec();

        DaxMessage message = req.createDictionaryReq();

        System.out.println(message.getType());
        System.out.println(codec.encode(message));

    }




}
