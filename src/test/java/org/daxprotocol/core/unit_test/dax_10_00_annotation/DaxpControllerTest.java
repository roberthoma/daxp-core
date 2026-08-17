package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpController;
import org.daxprotocol.core.annotation.DaxpHandler;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;

import java.util.List;

@DaxpController
public class DaxpControllerTest extends DaxConfigBaseTest {


    @DaxpHandler(DaxAnySchemaRegister.MSG_BASE_ENTITY_Req)
    public void getBaseData(DaxMessage incomeMsg, DaxFrame outcomeFrame){

        DaxAnyTestEntity testEntity  = new DaxAnyTestEntity("Test string",'H',456);
     //   DaxAnyTestEntity testEntity2 = new DaxAnyTestEntity("Test2 string2",'R',789);
//        List<DaxAnyTestEntity> testList = List.of(testEntity,testEntity2);
        List<DaxAnyTestEntity> testList = List.of(testEntity);

        DaxMessage message = msgFactory.toDaxMessage(incomeMsg,
                                        DaxAnySchemaRegister.MSG_BASE_ENTITY_DATA,
                                        testList);

        outcomeFrame.addMessage(message);
        outcomeFrame.addMessage(msgFactory.okMessage());

    }

}
