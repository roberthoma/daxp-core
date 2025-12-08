package Dax_DA_Dictionary_TEST;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.CustomerDaxTag;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Dax_Customer_dic_test01 extends DaxTestConfig {
    static String   msg;
    @BeforeAll
    static void initTest() {
          msg = "DAXP|V=1|EN=UTF8|\n" +
                "9=$DD|6=9|\n" +
                "7=1|209=Id customer|100=2001|110=I|\n" +
                "7=2|209=First name|100=2002|110=S|\n" +
                "7=3|209=Surname|100=2003|110=S|\n" +
                "7=4|209=Year of birth|100=2005|110=I|\n" +
                "7=5|209=Telephone|100=2073|110=S|\n" +
                "7=6|209=Town|100=2074|110=S|\n" +
                "7=7|209=Email|100=2011|110=S|\n" +
                "7=8|100=2074|103=I|105=Natural Person|\n" +
                "7=9|100=2074|103=O|105=Legal Entity|\n" +
                "99=123|";
    }

    @Test
    void testCustomerDicAttributes(){

        DaxDictionary dic = crmProvider.getDictionary();
        Map<Integer, DaxPair<?>> idAttrMap =  dic.getFieldAttributeMap(CustomerDaxTag.CUSTOMER_ID);
        Assertions.assertEquals("Id customer",idAttrMap.get(DaxTagConst.ATR_UI_LABEL).getStrValue());
        Assertions.assertEquals("I",idAttrMap.get(DaxTagConst.FIELD_DATA_TYPE).getStrValue());

        Map<Integer, DaxPair<?>> nameAttrMap = dic.getFieldAttributeMap(CustomerDaxTag.CUSTOMER_NAME);
        Assertions.assertEquals("First name",nameAttrMap.get(DaxTagConst.ATR_UI_LABEL).getStrValue());
        Assertions.assertEquals("S",nameAttrMap.get(DaxTagConst.FIELD_DATA_TYPE).getStrValue());

        Map<Integer, DaxPair<?>> telAttrMap = dic.getFieldAttributeMap(CustomerDaxTag.CUSTOMER_TELEPHONE);
        Assertions.assertEquals("Telephone",telAttrMap.get(DaxTagConst.ATR_UI_LABEL).getStrValue());
        Assertions.assertEquals("S",telAttrMap.get(DaxTagConst.FIELD_DATA_TYPE).getStrValue());

    }


   @Test
    void testCustomerDicDecoder(){

        DaxMessage msg = crmProvider.getMessageFactory()
                                    .dictionaryToMsg(crmProvider.getDictionary());

        String     msgStr    = crmProvider.getMessageCodec().encode(msg);
        DaxMessage msgAfter  = crmProvider.getMessageCodec().decode(msgStr);

        Assertions.assertEquals(msg.getBlockCount(),msgAfter.getBlockCount());

    }


    @Test
    public void testCustomerEntityEncoder(){
     DaxMessage message = crmProvider.getMessageCodec().decode(msg);
     Assertions.assertEquals(9,message.getBlockCount());
    }

}
