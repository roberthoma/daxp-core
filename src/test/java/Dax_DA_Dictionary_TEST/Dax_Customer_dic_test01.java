package Dax_DA_Dictionary_TEST;

import Dax_00_Base_test.CustomerDaxDic;
import org.daxprotocol.core.attributes.DaxAtrUiLabel;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPair;
import org.daxprotocol.core.codec.DaxTag;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Dax_Customer_dic_test01 {
    static String   msg;
    @BeforeAll
    static void initTest() {
          msg = "DAXP=1|TF=DEC|EN=UTF8|\n" +
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

        DaxDictionary customerDic = new CustomerDaxDic();

        Map<Integer, DaxPair<?>> idAttrMap = customerDic.getFieldAttributeMap(CustomerDaxDic.CUSTOMER_ID);
        Assertions.assertEquals("Id customer",idAttrMap.get(DaxTag.ATR_UI_LABEL).getStrValue());
        Assertions.assertEquals("I",idAttrMap.get(DaxTag.FIELD_DATA_TYPE).getStrValue());

        Map<Integer, DaxPair<?>> nameAttrMap = customerDic.getFieldAttributeMap(CustomerDaxDic.CUSTOMER_NAME);
        Assertions.assertEquals("First name",nameAttrMap.get(DaxTag.ATR_UI_LABEL).getStrValue());
        Assertions.assertEquals("S",nameAttrMap.get(DaxTag.FIELD_DATA_TYPE).getStrValue());

        Map<Integer, DaxPair<?>> telAttrMap = customerDic.getFieldAttributeMap(CustomerDaxDic.CUSTOMER_TELEPHONE);
        Assertions.assertEquals("Telephone",telAttrMap.get(DaxTag.ATR_UI_LABEL).getStrValue());
        Assertions.assertEquals("S",telAttrMap.get(DaxTag.FIELD_DATA_TYPE).getStrValue());

    }


    @Test
    void testCustomerDicEncoder(){

        DaxMessageFactory factory = new DaxMessageFactory();
        DaxDictionary customerDic = new CustomerDaxDic();
        DaxMessageCodec codec = new DaxMessageCodec();

        DaxMessage msg = factory.createDictionaryMsg(customerDic);
        System.out.println(msg);

        String msgStr = codec.encode(msg);
        DaxMessage msgAfter  = codec.decode(msgStr);
        System.out.println(msgStr);

        Assertions.assertEquals(9,msg.getBlockCount());

        Assertions.assertEquals(9,msgAfter.getBlockCount());



    }


    @Test
    public void testCustomerEntityEncoder(){

     DaxMessageCodec codec = new DaxMessageCodec();
     DaxMessage message = codec.decode(msg);

     int blocks =    message.getBody().getBlockCount();

     Assertions.assertEquals(9,blocks);

//        System.out.println(dic.getAttributeMap().get(CustomerDaxDic.CUSTOMER_SURNAME).getUiLabel());



    }

}
