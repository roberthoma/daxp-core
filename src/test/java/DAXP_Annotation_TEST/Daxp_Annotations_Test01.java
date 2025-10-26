package DAXP_Annotation_TEST;

import Dax_00_Base_test.Customer;
import Dax_00_Base_test.CustomerDaxDic;
import org.daxprotocol.core.annotations.DaxpTag;
import org.daxprotocol.core.codec.DaxTag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Daxp_Annotations_Test01 {


        @Test
        void shouldSeeAnnotationOnField() throws Exception {
            Field f = Customer.class.getDeclaredField("name");
            assertTrue(f.isAnnotationPresent(DaxpTag.class));
            DaxpTag ann = f.getAnnotation(DaxpTag.class);
            System.out.println("Ann tag : "+ann.tag());
            assertEquals(CustomerDaxDic.CUSTOMER_NAME, ann.tag());

            Customer  customer = new Customer();
            String value = "Robert";


            // make field accessible
            f.setAccessible(true);
            f.set(customer,value);

            System.out.println(customer.getName());

        }

        @Test
        void customer_tag_info(){
            Customer customer = new Customer();
            CustomerDaxDic dic = new CustomerDaxDic();
           try {
                for (Field field : Customer.class.getDeclaredFields()) {
                    if (field.isAnnotationPresent(DaxpTag.class)) {
                        DaxpTag daxp = field.getAnnotation(DaxpTag.class);
                        field.setAccessible(true);

                        System.out.println("\nTAG:   "+ daxp.tag());
                        System.out.println("Field name: " + field.getName());
                        System.out.println("Type class: " + field.getType());
                        System.out.println("Type simple name: " + field.getType().getSimpleName());
                        System.out.println("Is primitive: " + field.getType().isPrimitive());
                        var attMap =  dic.getFieldAttributeMap(daxp.tag());

                        System.out.println("Label: "+attMap.get(DaxTag.ATR_UI_LABEL).getValue() );
//                        System.out.println("Label: "+dic.getAttributeMap().get(daxp.tag()).getUiLabel());

                        //field.set(customer, "Tag=" + daxp.tag()); // any logic you want
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    @Test
    void customer_injection_val() {
    }
}
