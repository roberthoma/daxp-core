/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***********************************************************************
 */

package org.daxprotocol.core.datatype;

import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxDataTypeCodec {
    private static final Logger logger = LoggerFactory.getLogger(DaxDataTypeCodec.class);
    DaxDataTypeCollectionService dataTypeCollectionService;

    //--------------------------------------------------------------------------------------
    public DaxDataTypeCodec(DaxDataTypeCollectionService dataTypeCollectionService){
        this.dataTypeCollectionService = dataTypeCollectionService;


    }
    //--------------------------------------------------------------------------------------
//    private Class<?> decodeCOLLECTION(Map<DaxTag, DaxPair<?>> tagPairMap){
//
//        if (tagPairMap.containsKey(COLLECTION_HAS_KEY)) {
//            if (tagPairMap.get(COLLECTION_HAS_KEY).getBooleanValue()){
//                return Map.class;
//            }
//        };
//
//        if (tagPairMap.containsKey(COLLECTION_ALLOW_DUPLICATES)) {
//            if (tagPairMap.get(COLLECTION_ALLOW_DUPLICATES).getBooleanValue()){
//                return List.class;
//            }
//        };
//
//      return Set.class;
//    }


    //--------------------------------------------------------------------------------------
//    public Class<?> decode(Map<DaxTag, DaxPair<?>> tagPairMap){
//
//        if (tagPairMap.isEmpty()) return null;
//
//        if (!tagPairMap.containsKey(ATR_DATA_TYPE)) return null;
//        DaxDataType dataType = tagPairMap.get(ATR_DATA_TYPE).getDataTypeValue();
//
//        return switch (dataType){
//                      case COLLECTION ->  decodeCOLLECTION(tagPairMap);
//                      case STRING ->  String.class;
//                      case INTEGER ->  Integer.class;
//                      //todo develop
//                      default         -> null; /// TODO add log and exception
//        };
//
//    }

    //--------------------------------------------------------------------------------------
    public Set<DaxPair<?>> encode(Class<?> clazz){
        return encode(clazz, null);
    }
    //--------------------------------------------------------------------------------------

    public Set<DaxPair<?>> encode(Class<?> clazz, Type generitType){

        if (dataTypeCollectionService.isCollection(clazz)){
            return dataTypeCollectionService.collectionEncode(clazz,generitType);
        }

        //develop as generic collection
        Set< DaxPair<?>> map = new HashSet<>();

        map.add( new DaxPairDataType(ATR_DATA_TYPE,
                DaxDataType.fromCode(  dataTypeCollectionService.decodeClass(clazz).getCode())));
        return map;
    }
    //--------------------------------------------------------------------------------------
    public DaxDataType decodeBaseDataType(Object obj) {
        return dataTypeCollectionService.decodeClass(obj.getClass());
    }

    //--------------------------------------------------------------------------------------


    public Map<DaxTag, DaxPair<?>> encode(DaxDataType daxDataType) {
        throw new RuntimeException("Map<DaxTag, DaxPair<?>> encode  NOT IMPLEMENTED JED");
    }
    //--------------------------------------------------------------------------------------

}


