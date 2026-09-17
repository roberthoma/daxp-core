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
package org.daxprotocol.core.registries;

import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.model.pair.*;
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.*;
import static org.daxprotocol.core.application.DaxCoreTags.ATR_IS_DEPRECATED;
import static org.daxprotocol.core.application.DaxCoreTags.ATR_NULLABLE;
import static org.daxprotocol.core.application.DaxCoreTags.ATR_READONLY;
import static org.daxprotocol.core.application.DaxCoreTags.ATR_SIZE_MIN;
import static org.daxprotocol.core.application.DaxCoreTags.ENTRY_DESCRIPTION;
import static org.daxprotocol.core.application.DaxCoreTags.ENTRY_NAME;

//Wżne: klasa używana do rejestracji modelów: danych , komunikacji i implikacji także
//na poziomie parsowanych i przyjmowanych modeli z innych serviów .
// Nie używać metod rzutujących clasy czy typów JAVY

public class DaxSemanticCollector {
    private static final Logger logger = LoggerFactory.getLogger(DaxSemanticCollector.class);

    DaxSemanticRegistry semanticRegistry;
    DaxDataTypeService dataTypeService;
    DaxTagCodec tagCodec;
    DaxBaseRegistry<DaxTag> tagAttributes;
    ///----------------------------------------------------------------------------------------------
    public DaxSemanticCollector(DaxSemanticRegistry semanticRegistry,
                                DaxDataTypeService dataTypeService,
                                DaxTagCodec tagCodec
    ){
        this.semanticRegistry = semanticRegistry;
        this.dataTypeService = dataTypeService;
        this.tagCodec = tagCodec;
        tagAttributes = semanticRegistry.getTagAttributes();
    }

    ///----------------------------------------------------------------------------------------------

    // Critical exception (type mismatch) during semantic registration at application startup stops the application
    // During registration via message: log with a warning or an error

    public void registerDataType( DaxTag tag, DaxDataType daxDataType ){
        DaxDataType existDataType = semanticRegistry.getTagDataType(tag);

        if (!daxDataType.equals(DaxDataType.NONE)){
            putTagAtrDataType(tag,daxDataType);
        }
        else {
            if (existDataType != daxDataType){
                throw new RuntimeException("TAG EXIST WITH DATA TYPE "+ existDataType.getCode()+
                        " NEW:"+daxDataType.getCode());
            }

        }
    }


    ///---------------------------------------------------------------------
    public void putTagAttribute(DaxTag tag, DaxPair<?> atrPair){
        tagAttributes.putAttribute(tag, atrPair);
    }
    public void putTagAttributes(DaxTag tag, Set< DaxPair<?>> pairMap) {
        pairMap.forEach(( atrPair) -> putTagAttribute(tag,atrPair));
    };


    public void putTagAtrDataType(DaxTag tag, DaxDataType dataType)
    { putTagAttribute(tag, new DaxPairDataType(ATR_DATA_TYPE,dataType));}

    public void putRefTagAtrDataType(DaxTag tag, DaxDataType dataType)
    { putTagAttribute(tag, new DaxPairDataType(ATR_REF_TAG_ID,dataType));}

    public void putTagAtrSizeMax(DaxTag tag,  Integer max )
    { putTagAttribute(tag, new DaxPairInteger(ATR_SIZE_MAX,max));}

    public void putTagAtrSizeMin(DaxTag tag,  Integer min )
    { putTagAttribute(tag, new DaxPairInteger(ATR_SIZE_MIN,min));}

    public void putTagAtrNullable(DaxTag tag,  Boolean able)
    { putTagAttribute(tag, new DaxPairBoolean(ATR_NULLABLE ,able));}

    public void putTagAtrName(DaxTag tag,  String name){
        if(name!= null && !name.isBlank())
        {
            putTagAttribute(tag, new DaxPairString(ENTRY_NAME,name));
        }
    }

    public void putTagAtrDescription(DaxTag tag, String desc) {
        if(desc!= null && !desc.isBlank()){
            putTagAttribute(tag, new DaxPairString(ENTRY_DESCRIPTION,desc));
        }
    }

    public void putTagAtrReadOnly(DaxTag tag, Boolean able)
    { putTagAttribute(tag, new DaxPairBoolean(ATR_READONLY ,able));}

    public void putTagAtrDeprecated(DaxTag tag)
    { putTagAttribute(tag, new DaxPairBoolean(ATR_IS_DEPRECATED,true));}

//
    ///----------------------------------------------------------------------------------------------


    public void registerDataType(
            DaxTag tag,
            DaxTag entityTag,
            DaxDataType daxDataType
    )

    {
        DaxDataType existDataType = semanticRegistry.getTagDataType(tag);
        if(existDataType.equals(daxDataType)){
            return;
        }

        if(daxDataType.equals(DaxDataType.UNKNOWN)){
            logger.warn("DaxDataType.UNKNOWN tag={}",tagCodec.encode(tag));
            return;
        }


        if (existDataType == DaxDataType.NONE){


            putTagAtrDataType(tag,daxDataType);
            return;
        }

    }
    ///---------------------------------------------------------------------
    public void registerDescription(DaxTag tag, String description){

        putTagAtrDescription(tag, description);

    }
    ///---------------------------------------------------------------------

    public void registerName(DaxTag entityTag, DaxTag tag,  String name){

        semanticRegistry.putEntityEntryAtrName(entityTag,tag, name);

    }
    ///---------------------------------------------------------------------

    public void registerDescription(DaxTag entityTag, DaxTag tag,  String description){

        semanticRegistry.putEntityEntryAtrDescription(entityTag,tag, description);

    }
    ///---------------------------------------------------------------------


    public void registerReadOnly(DaxTag tag, boolean b) {
        putTagAtrReadOnly(tag, true);
    }
    ///---------------------------------------------------------------------


    public void putTagAtrNullable(DaxTag entityTag, DaxTag tag, boolean nullAble) {

        if (entityTag == null){
          putTagAtrNullable(tag,nullAble);
        }
        else {
          semanticRegistry.putEntityEntryAtrNullable(entityTag, tag, nullAble);
        }

    }
    ///---------------------------------------------------------------------

    public void putTagAtrSizeMin(DaxTag entityTag, DaxTag tag, int min) {
        if (entityTag == null){
            putTagAtrSizeMin(tag,min);
        }
        else {
            semanticRegistry.putEntityEntryAtrSizeMin(entityTag,tag,min);
        }

    }
    ///---------------------------------------------------------------------

    public void putTagAtrSizeMax(DaxTag entityTag, DaxTag tag, int max) {
        if (entityTag == null){
            putTagAtrSizeMax(tag,max);
        }
        else {
            semanticRegistry.putEntityEntryAtrSizeMax(entityTag,tag,max);
        }
    }

}
