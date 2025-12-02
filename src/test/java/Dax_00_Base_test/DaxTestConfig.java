package Dax_00_Base_test;

import Dax_00_Base_test.customer.CustomerDaxDic;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.config.DaxpPropertiesLoader;
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.junit.jupiter.api.BeforeAll;

public abstract class DaxTestConfig {

    public static DaxpConfig crmConfig;
    public static DaxpConfig cntConfig;

    public static DaxMessageCodec crmMessageCodec;
    public static DaxMessageCodec cntMessageCodec;

    public static DaxMessageConverter  crmMessageConverter;
    public static DaxMessageConverter  cntMessageConverter;

    public static DaxDictionary crmDictionary;
    public static DaxDictionary cntDictionary;

    @BeforeAll
    public static void initAll(){


    DaxpPropertiesLoader crmPropertiesLoader = new DaxpPropertiesLoader("application_CRM.properties");
    crmPropertiesLoader.load();
    crmConfig = new DaxpConfig();
    crmConfig.setApplicationContextId(crmPropertiesLoader.getApplicationContext());
    crmConfig.setTagFormat(crmPropertiesLoader.getTagFormat());
    crmConfig.setPairSeparator(crmPropertiesLoader.getPairSeparator());


    crmMessageCodec = new DaxMessageCodec(crmConfig);
    crmMessageConverter = new DaxMessageConverter(crmConfig);
    crmDictionary = new CustomerDaxDic(crmConfig);

    //------------------
    DaxpPropertiesLoader cntPropertiesLoader = new DaxpPropertiesLoader("application_CNT.properties");
    cntPropertiesLoader.load();
    cntConfig = new DaxpConfig();
    cntConfig.setApplicationContextId(cntPropertiesLoader.getApplicationContext());
    cntConfig.setTagFormat(cntPropertiesLoader.getTagFormat());
    cntConfig.setPairSeparator(cntPropertiesLoader.getPairSeparator());

    cntMessageCodec = new DaxMessageCodec(cntConfig);
    cntMessageConverter = new DaxMessageConverter(cntConfig);


    //cntDictionary;



//    DaxDictionary dictionary = new DaxDictionary();
//    dictionary.setContextMap(propertiesLoader.readContextMap());
//
//    DaxDictionaryPopulator manager = new DaxDictionaryPopulator();
//    DaxMessageFactory factory = new DaxMessageFactory();
//    DaxMessageCodec codec = new DaxMessageCodec();
//    AppMessage appMessage = new AppMessage();
//
//    DaxDictionary dicAfter = new DaxDictionary();


}

}
