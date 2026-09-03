package org.daxprotocol.core.unit_test.dax_00_01_base_config;

import org.daxprotocol.core.codec.DaxFrameCodec;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.registries.DaxMessageConverter;
import org.daxprotocol.core.registries.DaxSemanticRegistry;
import org.daxprotocol.core.dispatcher.DaxDispatcher;
import org.daxprotocol.core.registries.DaxHandlerRegistry;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.application.DaxEngine;

import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.unit_test.dax_10_00_annotation.*;
import org.junit.jupiter.api.*;

public class DaxConfigBaseTest {
    protected static DaxEngine daxEngine;
    protected static int appNamespaceId;
    protected static DaxSemanticRegistry dictionary;
    protected static DaxNamespaceMapper namespaceMapper;
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
    protected static DaxDataTypeService dataTypeService;



    @BeforeAll
    public static void initAll() {
        if (daxEngine == null) {

            daxEngine = new DaxEngine(DaxpConfigFactory
                    .createProperties("application_BASE.properties"));

            appNamespaceId = daxEngine.getConfig().getAppNamespaceId();
            dictionary      = daxEngine.getSemanticRegistry();
            namespaceMapper   = daxEngine.getnamespaceMapper();
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
            dataTypeService = daxEngine.getDataTypeService();

            System.out.println("*******************************************");
            System.out.println("      Base Application Configuration  << ");
            System.out.println(" Description  = "+ daxEngine.getConfig().getAppNamespaceDescription());
            System.out.println(" Symbol       = "+ daxEngine.getConfig().getAppNamespaceSymbol());
            System.out.println(" Tag Prefix   = "+ daxEngine.getConfig().getAppNamespaceTagPrefix());
            System.out.println(" namespace Id   = "+ daxEngine.getConfig().getAppNamespaceId());
            System.out.println("*******************************************");

            //------------
            daxEngine.register(DaxAnyTestDicEnum.class);
            daxEngine.register(DaxAnyTestEnum.class);

            daxEngine.register(DaxpControllerTest.class);
//            daxEngine.register(DaxSubEntity.class);
            daxEngine.register(DaxAnySchemaRegister.class);
//            daxEngine.register(DaxAnyTestEntity.class);
            handlerRegistry.registerCtrl(new DaxpControllerTest());  //Autowire in spring

            daxEngine.checkRegister();

        }
    }



//    @Test
//    void checknamespaceList(){
//        System.out.println("*************************************************");
//        System.out.println("               namespace list");
//        System.out.println();
//        daxEngine.getnamespaceMapper().getAllMappings().forEach((s, id) -> System.out.println(s +" id="+id));
//        System.out.println("*************************************************");
//    }

}
