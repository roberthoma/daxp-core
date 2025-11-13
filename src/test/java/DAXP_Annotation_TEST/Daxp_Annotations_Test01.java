package DAXP_Annotation_TEST;

import Dax_00_Base_test.Customer;
import Dax_00_Base_test.CustomerDaxDic;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.codec.DaxCodecSymbols;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.List;

public class Daxp_Annotations_Test01 {

    @Test
    void createMsgFromCustomer() {
        String expectMsg = "DAXP=1|TF=DEC|EN=UTF8|\n" +
                "9=UCi|2001=123|2002=Robert|\n" +
                "99=123|";

        Customer customer = new Customer(123, "Robert");
        DaxMessageFactory factory = new DaxMessageFactory();
        DaxMessage message = factory.toDaxMessage("UCi", customer);
        DaxMessageCodec codec = new DaxMessageCodec();
        String ecMsg = codec.encode(message);
        ecMsg= ecMsg.replace(DaxCodecSymbols.PAIR_SEPARATOR,'|');
        Assertions.assertEquals(expectMsg,ecMsg);
    }



    @Test
    void customer_tag_info(){
        CustomerDaxDic dic = new CustomerDaxDic();
        Customer customer = new Customer(123, "Robert");
       try {
            for (Field field : Customer.class.getDeclaredFields()) {
                System.out.println("Field> "+field.getName());
                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField daxp = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);

                    System.out.println("\nTAG:   "+ daxp.tag());
                    System.out.println("\nLABEL:   "+ daxp.uiLabel());
                    System.out.println("Field name: " + field.getName());
                    System.out.println("Type class: " + field.getType());
                    System.out.println("Type simple name: " + field.getType().getSimpleName());
                    System.out.println("Is primitive: " + field.getType().isPrimitive());
                    System.out.println("Pair: "+ daxp.tag()+"="+field.get(customer));

                    var attMap =  dic.getFieldAttributeMap(daxp.tag());
                    System.out.println("Label: "+attMap.get(org.daxprotocol.core.codec.DaxTag.ATR_UI_LABEL).getValue() );
//                        System.out.println("Label: "+dic.getAttributeMap().get(daxp.tag()).getUiLabel());

                    //field.set(customer, "Tag=" + daxp.tag()); // any logic you want
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    void injection(){
        String msgStr = "DAXP=1|TF=DEC|EN=UTF8|9=UCi|2001=123|2002=Robert|99=123|";
        DaxMessageCodec codec = new DaxMessageCodec();
        DaxMessage message = codec.decode(msgStr);
        Customer customer = DaxMessageConverter.fromMessage(message, Customer.class);
        Assertions.assertEquals("Robert" , customer.getName());
        Assertions.assertEquals(123 , customer.getCustomerId());
    }

}
