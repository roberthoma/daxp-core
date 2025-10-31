package Dax_00_Base_test;


import org.daxprotocol.core.dictionary.DaxDictionary;

public class CustomerDaxDic extends DaxDictionary {

    public static final int CUSTOMER_ID            = 2001;
    public static final int CUSTOMER_NAME          = 2002;
    public static final int CUSTOMER_SURNAME       = 2003;
    public static final int CUSTOMER_YEAR_OF_BIRTH = 2005;
    public static final int CUSTOMER_EMAIL         = 2011;
    public static final int CUSTOMER_TELEPHONE     = 2073;
    public static final int CUSTOMER_TOWN          = 2074;





    public CustomerDaxDic(){
        init();
    }

    private void putCustomerAtr(int tag, String uiLabel, Class<?> clazz) {
        super.put(tag, clazz);
        super.putAtrUiLabel(tag, uiLabel);
    }


    private void init() {
        putCustomerAtr(CUSTOMER_ID,"Id customer",Integer.class);
        putCustomerAtr(CUSTOMER_NAME,"First name",String.class);
        putCustomerAtr(CUSTOMER_SURNAME,"Surname",String.class);
        putCustomerAtr(CUSTOMER_YEAR_OF_BIRTH,"Year of birth",Integer.class);
        putCustomerAtr(CUSTOMER_EMAIL,"Email",String.class);
        putCustomerAtr(CUSTOMER_TELEPHONE,"Telephone",String.class);
        putCustomerAtr(CUSTOMER_TOWN,"Town",String.class);
    }
}
