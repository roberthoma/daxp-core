package org.daxprotocol.core.ut.dax_00_00_base_config;

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.conventer.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.parsers.DaxParser;
import org.daxprotocol.core.engine.DaxEngine;

import org.junit.jupiter.api.*;

public class DaxConfigBaseTest {
    protected static DaxEngine daxEngine;
    protected static DaxParser parser;
    protected static int appContextId;
    protected static DaxDictionary dictionary;
    protected static DaxContextMapper contextMapper;
    protected static DaxMessageCodec messageCodec;
    protected static DaxPreambleCodec preambleCodec;
    protected static DaxConfig config;
    protected static DaxTagCodec tagCodec;
    protected static DaxMessageConverter msgConverter;
    protected static DaxMessageFactory msgFactory;
    protected static DaxHandlerRegistry handlerRegistry;



    @BeforeAll
    public static void initAll() {
        if (daxEngine == null) {
            daxEngine = new DaxEngine(DaxpConfigFactory
                    .createConfig(DaxpConfigFactory
                            .createProperties("application_BASE.properties")));

            parser        = daxEngine.getParser();
            appContextId  = daxEngine.getConfig().getAppContextId();
            dictionary    = daxEngine.getDictionary();
            contextMapper = daxEngine.getContextMapper();
            messageCodec  = daxEngine.getMessageCodec();
            preambleCodec = daxEngine.getPreambleCodec();
            config        = daxEngine.getConfig();
            tagCodec      = daxEngine.getTagCodec();
            msgConverter  = daxEngine.getMessageConverter();
            msgFactory    = daxEngine.getMessageFactory();
            handlerRegistry = daxEngine.getHandlerRegistry();

            System.out.println("*******************************************");
            System.out.println("      Base Application Configuration  << ");
            System.out.println(" Description  = "+ daxEngine.getConfig().getAppContextDescription());
            System.out.println(" Symbol       = "+ daxEngine.getConfig().getAppContextSymbol());
            System.out.println(" Tag Prefix   = "+ daxEngine.getConfig().getAppContextTagPrefix());
            System.out.println(" Context Id   = "+ daxEngine.getConfig().getAppContextId());
            System.out.println("*******************************************");

        }
    }

    @Order(1)
    @Test
    void checkAppContextId(){
        Assertions.assertEquals(1, daxEngine.getConfig().getAppContextId());
    }

    @Test
    void checkAppTagPrefix(){
        String appCtx = "XYZ";
        Assertions.assertEquals(appCtx, config.getAppContextTagPrefix());
        Assertions.assertEquals(config.getAppContextId(),contextMapper.getReferenceId(appCtx));
    }


    @AfterAll
    static void checkContextList(){
        System.out.println("*************************************************");
        System.out.println("               Context list");
        System.out.println();
        daxEngine.getContextMapper().getAllMappings().forEach((s, id) -> System.out.println(s +" id="+id));
        System.out.println("*************************************************");
    }

}
