package DAXP_Annotation_TEST;

import Dax_00_Base_test.DaxTestConfig;
import Dax_00_Base_test.address.Address;
import Dax_00_Base_test.address.District;
import Dax_00_Base_test.crm_application.customer.Customer;
import Dax_00_Base_test.crm_application.customer.CustomerDaxSchema;
import Dax_00_Base_test.crm_application.customer.CustomerRelation;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.DaxMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class Daxp_Annotations_Test01 extends DaxTestConfig {

    @Test
    void createMsgFromCustomer() {
        String expectMsg = "DAXP="+DaxpConfig.PROTOCOL_VERSION+"|EN=UTF-8|CX=CRM|\n" +
        "9=UCi|5=INST|100=2000|2080=Big bike|2001=123|2002=Robert|2085=23|2076=WORKER|2077=Y|\n"+
                "99=169|";
        System.out.println("--------------------------------------------------->>>");
        System.out.println("   Generation parallel message");

        Customer customer = new Customer(123, "Robert");
        customer.setRelation(CustomerRelation.WORKER);
        customer.setCitizen(true);

        DaxMessage message = crmProvider.getMessageFactory().toDaxMessage("UCi", customer);
        String ecMsg = crmProvider.getMessageCodec().encode(message);

        ecMsg= ecMsg.replace(DaxpConfig.PAIR_SEPARATOR,'|');

        System.out.println("EXPECTED : "+ expectMsg);
        System.out.println("WAS : "+ ecMsg);
        System.out.println("<<<<---------------------------------------------------");

        Assertions.assertEquals(expectMsg,ecMsg);
    }



    @Test
    void customer_tag_info(){
//        CustomerDaxDic dic = new CustomerDaxDic();
        Customer customer = new Customer(123, "Robert");
       try {
            for (Field field : Customer.class.getDeclaredFields()) {
//                System.out.println("------------------------");
//                System.out.println("Field> "+field.getName());
                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField daxp = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);

/*                    System.out.println("TAG:   "+ daxp.tagId());
                    System.out.println("LABEL:   "+ daxp.uiLabel());
                    System.out.println("Field name: " + field.getName());
                    System.out.println("Type class: " + field.getType());
                    System.out.println("Type simple name: " + field.getType().getSimpleName());
                    System.out.println("Is primitive: " + field.getType().isPrimitive());
                    System.out.println("Pair: "+ daxp.tagId()+"="+field.get(customer));
*/
                    var attMap =  crmProvider.getDictionary().getFieldAttributeMap(daxp.value());

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

    @Test
    void injection(){
        String msgStr = "DAXP=1|EN=UTF-8|9=UCi|20=1|2001=123|2002=Robert|2075=INDIVIDUAL|99=123|";
        System.out.println("BEFORE: "+msgStr);
        DaxMessage message = crmProvider.getMessageCodec().decode(msgStr);

        Customer customer = crmProvider.getMessageConverter().createFromMessage(message, Customer.class);
        Assertions.assertEquals("Robert" , customer.getName());
        Assertions.assertEquals(123 , customer.getCustomerId());

        DaxMessage updMsg = crmProvider.getMessageCodec()
                                       .decode("DAXP=1|EN=UTF-8|9=CU|5=I|2001=123|2002=Jan|2074=Toronto|99=123|\"");
        System.out.println("UPDATE MSG: "+msgStr);
        crmProvider.getMessageConverter().updateFromMessage(updMsg, customer );
        Assertions.assertEquals("Jan",customer.getName());
       // Assertions.assertEquals("Toronto",customer.getTown());

        DaxMessage msg2 = crmProvider.getMessageFactory().toDaxMessage("CMD",customer);
        System.out.println("AFTER :"+ crmProvider.getMessageCodec().encode(msg2));

    }

    @Test
    void addressTest(){
        System.out.println("ADDRESS setting");
        String msgStr = "DAXP=1|EN=UTF-8|9=UCi|20=1|2001=123|2002=Robert|2075=INDIVIDUAL|99=123|";
        System.out.println("BEFORE: "+msgStr);
        DaxMessage message = crmProvider.getMessageCodec().decode(msgStr);
        Customer customer = crmProvider.getMessageConverter().createFromMessage(message, Customer.class);

        customer.address = new Address();
        customer.address.town = "Warszawa";
        customer.address.street = "Polna 7";
        customer.address.addressId = 345;


        customer.corresp_address = new Address();
        customer.corresp_address.town = "Sanok";
        customer.corresp_address.street = "Lipińskiego 1000";
        customer.corresp_address.addressId = 3346;
        customer.corresp_address.district = new District("43-444","Zakopane");


        DaxMessage message2 = crmProvider.getMessageFactory().toDaxMessage(CustomerDaxSchema.CRM_DTO, customer);
        DaxMessage message3 = crmProvider.getMessageFactory().toDaxMessage(CustomerDaxSchema.CRM_DTO, customer.address);
        DaxMessage message4 = crmProvider.getMessageFactory().toDaxMessage(CustomerDaxSchema.CRM_DTO, customer.corresp_address);

        System.out.println("----------------------");
        System.out.println("Customer and address: msg");
        System.out.println(crmProvider.getMessageCodec().encode(message2));
        System.out.println("----------------------");
        System.out.println("Address: msg");

        System.out.println(crmProvider.getMessageCodec().encode(message3));

    }

}
