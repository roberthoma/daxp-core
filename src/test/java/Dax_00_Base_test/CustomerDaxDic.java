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

    private void putCustAtr(int tag, String uiLabel, Class<?> clazz) {
        put(tag, clazz);
        putAtrUiLabel(tag, uiLabel);
    }


    private void init() {
        putCustAtr(CUSTOMER_ID,"Id customer",Integer.class);
        putCustAtr(CUSTOMER_NAME,"First name",String.class);
        putCustAtr(CUSTOMER_SURNAME,"Surname",String.class);
        putCustAtr(CUSTOMER_YEAR_OF_BIRTH,"Year of birth",Integer.class);
        putCustAtr(CUSTOMER_EMAIL,"Email",String.class);
        putCustAtr(CUSTOMER_TELEPHONE,"Telephone",String.class);
        putCustAtr(CUSTOMER_TOWN,"Town",String.class);
    }
}
