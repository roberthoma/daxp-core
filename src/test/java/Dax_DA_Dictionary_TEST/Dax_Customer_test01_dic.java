package Dax_DA_Dictionary_TEST;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.CustomerDaxTag;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Dax_Customer_test01_dic extends DaxTestConfig {
    @Test
    void testCustomerDicAttributes(){

        DaxDictionary dic = cmrProvider.getDictionary();

        Map<DaxTag, DaxPair<?>> idAttrMap =  dic.getFieldAttributeMap(CustomerDaxTag.CUSTOMER_ID);

        Assertions.assertEquals("Id customer",idAttrMap.get(DaxTagConst.ATR_UI_LABEL_TAG).getStrValue());
        Assertions.assertEquals("I",idAttrMap.get(DaxTagConst.FIELD_DATA_TYPE_TAG).getStrValue());

        Map<DaxTag, DaxPair<?>> nameAttrMap = dic.getFieldAttributeMap(CustomerDaxTag.CUSTOMER_NAME);
        Assertions.assertEquals("First name",nameAttrMap.get(DaxTagConst.ATR_UI_LABEL_TAG).getStrValue());
        Assertions.assertEquals("S",nameAttrMap.get(DaxTagConst.FIELD_DATA_TYPE_TAG).getStrValue());

        Map<DaxTag, DaxPair<?>> telAttrMap = dic.getFieldAttributeMap(CustomerDaxTag.CUSTOMER_TELEPHONE);
        Assertions.assertEquals("Telephone",telAttrMap.get( DaxTagConst.ATR_UI_LABEL_TAG).getStrValue());
        Assertions.assertEquals("S",telAttrMap.get(DaxTagConst.FIELD_DATA_TYPE_TAG).getStrValue());


    }

}
