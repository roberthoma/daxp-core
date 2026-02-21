package DAXP_Annotation_TEST;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.customer.Customer;
import Dax_00_Base_test.customer.CustomerRelation;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;

public class Daxp_Annotations_Test01 extends DaxTestConfig {

    //@Test
    void createMsgFromCustomer() {
        String expectMsg = "DAXP|V=1|EN=UTF-8|\n" +
                "9=UCi|20=1|2001=123|2002=Robert|2076=WORKER|2077=Y|\n" +
                "99=123|";

        Customer customer = new Customer(123, "Robert");
        customer.setRelation(CustomerRelation.WORKER);
        customer.setCitizen(true);
        DaxMessage message = cmrProvider.getMessageFactory().toDaxMessage("UCi", customer);
        String ecMsg = cmrProvider.getMessageCodec().encode(message);
        ecMsg= ecMsg.replace(DaxpConfig.PAIR_SEPARATOR,'|');
        Assertions.assertEquals(expectMsg,ecMsg);
    }



    //@Test
    void customer_tag_info(){
//        CustomerDaxDic dic = new CustomerDaxDic();
        Customer customer = new Customer(123, "Robert");
       try {
            for (Field field : Customer.class.getDeclaredFields()) {
                System.out.println("------------------------");
                System.out.println("Field> "+field.getName());
                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField daxp = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);

                    System.out.println("TAG:   "+ daxp.tagId());
                    System.out.println("LABEL:   "+ daxp.uiLabel());
                    System.out.println("Field name: " + field.getName());
                    System.out.println("Type class: " + field.getType());
                    System.out.println("Type simple name: " + field.getType().getSimpleName());
                    System.out.println("Is primitive: " + field.getType().isPrimitive());
                    System.out.println("Pair: "+ daxp.tagId()+"="+field.get(customer));

                    var attMap =  cmrProvider.getDictionary().getFieldAttributeMap(daxp.tagId());

//                    System.out.println("Label: "+ Optional.of(attMap.get(org.daxprotocol.core.codec.DaxTag.ATR_UI_LABEL))
//                                    .
                            //.getValue() );
//                        System.out.println("Label: "+dic.getAttributeMap().get(daxp.tag()).getUiLabel());

                    //field.set(customer, "Tag=" + daxp.tag()); // any logic you want
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //@Test
    void injection(){
        String msgStr = "DAXP|V=1|EN=UTF-8|9=UCi|20=1|2001=123|2002=Robert|2075=INDIVIDUAL|99=123|";

        DaxMessage message = cmrProvider.getMessageCodec().decode(msgStr);

        Customer customer = cmrProvider.getMessageConverter().createFromMessage(message, Customer.class);
        Assertions.assertEquals("Robert" , customer.getName());
        Assertions.assertEquals(123 , customer.getCustomerId());

        DaxMessage updMsg = cmrProvider.getMessageCodec().decode("DAXP|V=1|EN=UTF-8|9=CU|20=1|2001=123|2002=Jan|2074=Toronto|99=123|\"");

        cmrProvider.getMessageConverter().updateFromMessage(updMsg, customer );
        Assertions.assertEquals("Jan",customer.getName());
        Assertions.assertEquals("Toronto",customer.getTown());

    }

}
