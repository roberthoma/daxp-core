package Dax_DA_Dictionary_TEST;

import Dax_00_Base_test.DaxTestConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Dax_Customer_test30_dic_from_msg extends DaxTestConfig {
    static String   msg;
    @BeforeAll
    static void initTest() {
        msg = "DAXP|V=1|EN=UTF-8|\n" +
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
    public void testCustomerEntityEncoder(){
        DaxMessage message = crmProvider.getMessageCodec().decode(msg);
        Assertions.assertEquals(9,message.getBlockCount());
    }

}
