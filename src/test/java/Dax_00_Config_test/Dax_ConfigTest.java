package Dax_00_Config_test;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.config.DaxpPropertiesLoader;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Dax_ConfigTest {

    @Test
    public void test_reading_properties(){

        DaxDictionary dictionary = new DaxDictionary();

        DaxpPropertiesLoader propertiesLoader = new DaxpPropertiesLoader("application.properties");
        propertiesLoader.load();

        DaxpConfig.setApplicationContextId(propertiesLoader.readApplicationContext());

        Assertions.assertEquals(2,DaxpConfig.getApplicationContextId());

        dictionary.setContextMap(propertiesLoader.readContextMap());

        dictionary.getContextMap().forEach((integer, daxContext) ->
                        System.out.println("CTX : "+daxContext.description)
                );


    }
}
