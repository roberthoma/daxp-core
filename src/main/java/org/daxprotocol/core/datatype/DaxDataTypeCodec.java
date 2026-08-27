/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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

import org.daxprotocol.core.annotation.DaxpCollection;
import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.daxprotocol.core.application.DaxCoreTags.*;

public class DaxDataTypeCodec {
    private static final Logger logger = LoggerFactory.getLogger(DaxDataTypeCodec.class);
    DaxDataTypeService dataTypeService;
    DaxTagCodec tagCodec;


    //--------------------------------------------------------------------------------------
    public DaxDataTypeCodec(DaxDataTypeService dataTypeService, DaxTagCodec tagCodec){
        this.dataTypeService = dataTypeService;
        this.tagCodec = tagCodec;

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

        if (dataTypeService.isCollection(clazz)){
            return collectionEncode(clazz,generitType);
        }

        //develop as generic collection
        Set< DaxPair<?>> map = new HashSet<>();

        map.add( new DaxPairDataType(ATR_DATA_TYPE,
                DaxDataType.fromCode(  dataTypeService.decodeClass(clazz).getCode())));
        return map;
    }
    //--------------------------------------------------------------------------------------
    public DaxDataType decodeBaseDataType(Object obj) {
        return dataTypeService.decodeClass(obj.getClass());
    }
    //--------------------------------------------------------------------------------------
    public boolean isCollection(Object object){
        return dataTypeService.decodeFromObject(object).equals(DaxDataType.COLLECTION) ;
    }
    //--------------------------------------------------------------------------------------
    public boolean isCollection(Class<?> clazz){
        return dataTypeService.decodeClass(clazz).equals(DaxDataType.COLLECTION) ;
    }

    //--------------------------------------------------------------------------------------
    public boolean isPrimitiveType(Object obj){
        return  dataTypeService.isPrimitiveType(obj.getClass());
    }

    //--------------------------------------------------------------------------------------


    public Map<DaxTag, DaxPair<?>> encode(DaxDataType daxDataType) {
        throw new RuntimeException("Map<DaxTag, DaxPair<?>> encode  NOT IMPLEMENTED JED");
    }

    public boolean isMap(Object object) {
        DaxCollectionInfo info = dataTypeService.getCollectionInfo(object.getClass());

        return info.isColHasKey && !info.isJavaEnum;
    }
    //--------------------------------------------------------------------------------------
    public Set<DaxPair<?>> collectionEncode(Class<?> clazz, Type generitType){
        Set< DaxPair<?>> map = new HashSet<>();
        DaxCollectionInfo colInfo = dataTypeService.getCollectionInfo(clazz);

        map.add(new DaxPairDataType(ATR_DATA_TYPE,DaxDataType.COLLECTION));

        if(colInfo.isColAllowDuplicates)  map.add(new DaxPairBoolean(COLLECTION_ALLOW_DUPLICATES,true));
        if(colInfo.isColHasKey)           map.add(new DaxPairBoolean(COLLECTION_HAS_KEY,true));
        if(colInfo.isColDictionary)       map.add(new DaxPairBoolean(COLLECTION_IS_DICTIONARY,true));
        if(colInfo.isColNavigable)        map.add(new DaxPairBoolean(COLLECTION_NAVIGABLE,true));


        //**************************************

        DaxDataType valueDataType = DaxDataType.NONE;
        DaxDataType keyDataType = DaxDataType.NONE;

        if (generitType instanceof ParameterizedType pt) {

            logger.info( "GeneritType.getTypeName()= {}", generitType.getTypeName());

            Type rawType = pt.getRawType();
            Type[] args = pt.getActualTypeArguments();
//            Annotation ann;
            if (colInfo.isColHasKey){
                keyDataType   = dataTypeService.decodeClass( args[0]);
                valueDataType = dataTypeService.decodeClass( args[1]);

                map.add(new DaxPairDataType(COLLECTION_KEY_DATA_TYPE,keyDataType));
                if(keyDataType.equals(DaxDataType.ENTITY))
                {
                    DaxpEntity entAnn =  dataTypeService.castReflectTypeToClass(args[0]).getAnnotation(DaxpEntity.class);
                    map.add(new DaxPairTag(COLLECTION_KEY_TYPE_REF_ID,tagCodec.decode(entAnn)));
                }


                map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,valueDataType));

                if(valueDataType.equals(DaxDataType.ENTITY))
                {
                    DaxpEntity entAnn =  dataTypeService.castReflectTypeToClass(args[1]).getAnnotation(DaxpEntity.class);
                    map.add(new DaxPairTag(COLLECTION_VALUE_TYPE_REF_ID,tagCodec.decode(entAnn)));
                }


                if(valueDataType.equals(DaxDataType.COLLECTION))
                {
                    if (dataTypeService.castReflectTypeToClass(args[1]).isAnnotationPresent(DaxpCollection.class)){
                        DaxpCollection colAnn =  dataTypeService.castReflectTypeToClass(args[1])
                                                                .getAnnotation(DaxpCollection.class);
                        map.add(new DaxPairTag(COLLECTION_VALUE_TYPE_REF_ID,tagCodec.decode(colAnn)));
                    }
                }


            }else {
                valueDataType = dataTypeService.decodeClass( args[0]);
                map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,valueDataType));
                if(valueDataType.equals(DaxDataType.ENTITY))
                {
                    DaxpEntity entAnn =  dataTypeService.castReflectTypeToClass(args[0]).getAnnotation(DaxpEntity.class);
                    map.add(new DaxPairTag(COLLECTION_VALUE_TYPE_REF_ID,tagCodec.decode(entAnn)));
                }

            }
        }

        if(colInfo.isJavaEnum){
            map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,DaxDataType.STRING));
        }

        return map;

    }
    //--------------------------------------------------------------------------------------

}


