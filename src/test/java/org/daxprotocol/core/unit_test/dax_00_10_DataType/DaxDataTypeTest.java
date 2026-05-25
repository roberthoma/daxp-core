package org.daxprotocol.core.unit_test.dax_00_10_DataType;

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.pair.DaxPairBoolean;
import org.daxprotocol.core.model.pair.DaxPairDataType;
import org.daxprotocol.core.unit_test.dax_00_01_base_config.DaxConfigBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.*;
public class DaxDataTypeTest extends DaxConfigBaseTest {



    @Test
    void classNullToUnknow() {

        DaxDataType classType =  dataTypeService.decodeClass(null);
        Assertions.assertEquals(DaxDataType.UNKNOWN.getCode(),classType.getCode());
    }


    @Test
    void classStringToSTR() {

        DaxDataType classType =  dataTypeService.decodeClass(String.class);
        Assertions.assertEquals(DaxDataType.STRING.getCode(),classType.getCode());
    }


    @Test
    void mapCollectionList() {
        Map<DaxTag, DaxPair<?>> tagPairMap = new HashMap<>();
        Set< DaxPair<?>> tagPairMapOut ;

        tagPairMap.put(ATR_DATA_TYPE, new DaxPairDataType(ATR_DATA_TYPE,DaxDataType.COLLECTION));
        tagPairMap.put(COLLECTION_ALLOW_DUPLICATES, new DaxPairBoolean(COLLECTION_ALLOW_DUPLICATES,true));

        Class<?> clazz =  dataTypeCodec.decode(tagPairMap);
        Assertions.assertEquals(List.class, clazz);

//        tagPairMapOut = dataTypeCodec.encode(List.class);
//
//        tagPairMapOut.forEach(( daxPair) ->
//                System.out.println(tagCodec.encode(daxPair.getTag())  +"="+  daxPair.getStrValue()));


      //  Assertions.assertEquals(tagPairMap, tagPairMapOut);


    }





}
