package org.daxprotocol.core.unit_test.dax_00_01_base_config;

import org.daxprotocol.core.codec.DaxFrameCodec;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.datatype.DaxDataTypeCollectionService;
import org.daxprotocol.core.dictionary.DaxMessageConverter;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dispatcher.DaxDispatcher;
import org.daxprotocol.core.dispatcher.DaxHandlerRegistry;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.application.DaxEngine;

import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.unit_test.dax_10_00_annotation.*;
import org.junit.jupiter.api.*;

public class DaxConfigBaseTest {
    protected static DaxEngine daxEngine;
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
    protected static DaxFrameCodec frameCodec;
    protected static DaxTagParser tagParser;
    protected static DaxFrameParser frameParser;
    protected static DaxPreambleFactory preambleFactory;
    protected static DaxDispatcher dispatcher;
    protected static DaxDataTypeCodec dataTypeCodec;
    protected static DaxDataTypeCollectionService dataTypeService;



    @BeforeAll
    public static void initAll() {
        if (daxEngine == null) {
            daxEngine = new DaxEngine(DaxpConfigFactory
                    .createConfig(DaxpConfigFactory
                            .createProperties("application_BASE.properties")));

            appContextId    = daxEngine.getConfig().getAppContextId();
            dictionary      = daxEngine.getDictionary();
            contextMapper   = daxEngine.getContextMapper();
            messageCodec    = daxEngine.getMessageCodec();
            preambleCodec   = daxEngine.getPreambleCodec();
            config          = daxEngine.getConfig();
            tagCodec        = daxEngine.getTagCodec();
            msgConverter    = daxEngine.getMessageConverter();
            msgFactory      = daxEngine.getMessageFactory();
            handlerRegistry = daxEngine.getHandlerRegistry();
            frameCodec      = daxEngine.getFrameCodec();
            tagParser       = daxEngine.getTagParser();
            frameParser     = daxEngine.getFrameParser();
            preambleFactory = daxEngine.getPreambleFactory();
            dispatcher      = daxEngine.getDispatcher();
            dataTypeCodec   = daxEngine.getDataTypeCodec();
            dataTypeService = daxEngine.getDataTypeCollectionService();

            System.out.println("*******************************************");
            System.out.println("      Base Application Configuration  << ");
            System.out.println(" Description  = "+ daxEngine.getConfig().getAppContextDescription());
            System.out.println(" Symbol       = "+ daxEngine.getConfig().getAppContextSymbol());
            System.out.println(" Tag Prefix   = "+ daxEngine.getConfig().getAppContextTagPrefix());
            System.out.println(" Context Id   = "+ daxEngine.getConfig().getAppContextId());
            System.out.println("*******************************************");

            //------------
            daxEngine.register(DaxpControllerTest.class);
            daxEngine.register(DaxAnyTestEnum.class);
            daxEngine.register(DaxSubEntity.class);
            daxEngine.register(DaxAnyTestDicEnum.class);
            daxEngine.register(DaxpManifest_Base.class);
            daxEngine.register(DaxAnyTestEntity.class);
            handlerRegistry.registerCtrl(new DaxpControllerTest());  //Autowire in spring

            daxEngine.checkRegister();

        }
    }



//    @Test
//    void checkContextList(){
//        System.out.println("*************************************************");
//        System.out.println("               Context list");
//        System.out.println();
//        daxEngine.getContextMapper().getAllMappings().forEach((s, id) -> System.out.println(s +" id="+id));
//        System.out.println("*************************************************");
//    }

}
