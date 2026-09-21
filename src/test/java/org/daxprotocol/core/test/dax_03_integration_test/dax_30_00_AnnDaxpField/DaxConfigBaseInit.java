package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.application.DaxCoreMessages;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.application.DaxEngine;
import org.daxprotocol.core.codec.DaxFrameCodec;
import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.dispatcher.DaxDispatcher;
import org.daxprotocol.core.factory.DaxMessageFactory;
import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.parsers.DaxFrameParser;
import org.daxprotocol.core.parsers.DaxTagParser;
import org.daxprotocol.core.registries.DaxHandlerRegistry;
import org.daxprotocol.core.registries.DaxMessageConverter;
import org.daxprotocol.core.registries.DaxSemanticRegistry;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DaxConfigBaseInit {
    protected  DaxEngine daxEngine;
    protected  int appNamespaceId;
    protected  DaxSemanticRegistry semanticRegistry;
    protected  DaxNamespaceMapper namespaceMapper;
    protected  DaxMessageCodec messageCodec;
    protected  DaxPreambleCodec preambleCodec;
    protected  DaxConfig config;
    protected  DaxTagCodec tagCodec;
    protected  DaxMessageConverter msgConverter;
    protected  DaxMessageFactory msgFactory;
    protected  DaxHandlerRegistry handlerRegistry;
    protected  DaxFrameCodec frameCodec;
    protected  DaxTagParser tagParser;
    protected  DaxFrameParser frameParser;
    protected  DaxPreambleFactory preambleFactory;
    protected  DaxDispatcher dispatcher;
    protected  DaxDataTypeCodec dataTypeCodec;
    protected  DaxDataTypeService dataTypeService;




    @BeforeEach
    public  void initAll() {
        if (daxEngine == null) {

            daxEngine = new DaxEngine(DaxpConfigFactory
                    .createProperties("application_BASE.properties"));

            appNamespaceId = daxEngine.getConfig().getAppNamespaceId();
            semanticRegistry = daxEngine.getSemanticRegistry();
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

            System.out.println("INIT ENGINE");

        }
    }

    protected void printSemanticRegister(){
        String reqMsg = "DAXP|$:1="+ DaxCoreMessages.DATA_MODEL_REQ +"|$:9=148|";
        DaxFrame frameReq = frameParser.parseFrame(reqMsg);
        DaxFrame frameResp = new DaxFrame();
        frameResp.setPreamble(preambleFactory.createRespPreamble(frameReq));
        handlerRegistry.executor(frameReq, frameResp);
        DaxMessage respMsg = frameResp.getFirstMessage();
        System.out.println("-------------------\n");
        System.out.println("REQ > " + reqMsg);
        System.out.println("RES > " + DaxMessageDecorator.decorate(frameCodec.encode(frameResp)));
        System.out.println("AFTER DIC");
    }

}
