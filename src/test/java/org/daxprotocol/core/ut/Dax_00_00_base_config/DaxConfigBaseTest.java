package org.daxprotocol.core.ut.Dax_00_00_base_config;

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.parsers.DaxParser;
import org.daxprotocol.core.provider.DaxProvider;

import org.junit.jupiter.api.*;

public class DaxConfigBaseTest {
    protected static DaxProvider provider;
    protected static DaxParser parser;
    protected static int appContextId;
    protected static DaxDictionary dictionary;
    protected static DaxContextMapper contextMapper;
    protected static DaxMessageCodec messageCodec;
    protected static DaxPreambleCodec preambleCodec;

    @BeforeAll
    public static void initAll() {
        if (provider == null) {
            provider = new DaxProvider(DaxpConfigFactory
                    .createConfig(DaxpConfigFactory
                            .createProperties("application_BASE.properties")));

            parser        = provider.getParserService();
            appContextId  = provider.getConfig().getAppContextId();
            dictionary    = provider.getDictionary();
            contextMapper = provider.getContextMapper();
            messageCodec  = provider.getMessageCodec();
            preambleCodec = provider.getPreambleCodec();


            System.out.println("*******************************************");
            System.out.println("      Base Application Configuration  << ");
            System.out.println(" Description  = "+ provider.getConfig().getAppContextDescription());
            System.out.println(" Symbol       = "+ provider.getConfig().getAppContextSymbol());
            System.out.println(" Tag Prefix   = "+ provider.getConfig().getAppContextTagPrefix());
            System.out.println(" Context Id   = "+ provider.getConfig().getAppContextId());
            System.out.println("*******************************************");

        }
    }

    @Order(1)
    @Test
    void checkAppContextId(){
        Assertions.assertEquals(1, provider.getConfig().getAppContextId());
    }

    @AfterAll
    static void checkContextList(){
        System.out.println("*************************************************");
        System.out.println("               Context list");
        System.out.println();
        provider.getContextMapper().getAllMappings().forEach((s, id) -> System.out.println(s +" id="+id));
        System.out.println("*************************************************");
    }

}
