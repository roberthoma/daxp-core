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
import org.daxprotocol.core.model.tag.DaxTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Wżne: klasa używana do rejestracji modelów: danych , komunikacji i implikacji także
//na poziomie parsowanych i przyjmowanych modeli z innych serviów .
// Nie używać metod rzutujących clasy czy typów JAVY

public class DaxSemanticCollector {
    private static final Logger logger = LoggerFactory.getLogger(DaxSemanticCollector.class);

    DaxSemanticRegistry semanticRegistry;
    DaxDataTypeService dataTypeService;
    DaxTagCodec tagCodec;
    ///----------------------------------------------------------------------------------------------
    public DaxSemanticCollector(DaxSemanticRegistry semanticRegistry,
                                DaxDataTypeService dataTypeService,
                                DaxTagCodec tagCodec
    ){
        this.semanticRegistry = semanticRegistry;
        this.dataTypeService = dataTypeService;
        this.tagCodec = tagCodec;
    }


// Critical exception (type mismatch) during semantic registration at application startup stops the application
// During registration via message: log with a warning or an error

    ///----------------------------------------------------------------------------------------------

    public void registerDataType( DaxTag tag, DaxDataType daxDataType ){
        DaxDataType existDataType = semanticRegistry.getTagDataType(tag);

        if (!daxDataType.equals(DaxDataType.NONE)){
            semanticRegistry.putTagAtrDataType(tag,daxDataType);
        }
        else {
            if (existDataType != daxDataType){
                throw new RuntimeException("TAG EXIST WITH DATA TYPE "+ existDataType.getCode()+
                        " NEW:"+daxDataType.getCode());
            }

        }
    }
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


            semanticRegistry.putTagAtrDataType(tag,daxDataType);
            return;
        }

    }
    ///---------------------------------------------------------------------
    public void registerDescription(DaxTag tag, String description){

        semanticRegistry.putTagAtrDescription(tag, description);

    }
    ///---------------------------------------------------------------------

    public void registerDescription(DaxTag tag, DaxTag entityTag, String description){

        semanticRegistry.putEntityEntryAtrDescription(entityTag,tag, description);

    }
    ///---------------------------------------------------------------------


    public void registerReadOnly(DaxTag tag, boolean b) {
        semanticRegistry.putTagAtrReadOnly(tag, true);
    }
    ///---------------------------------------------------------------------


    public void putTagAtrNullable(DaxTag entityTag, DaxTag tag, boolean nullAble) {

        if (entityTag == null){
          semanticRegistry.putTagAtrNullable(tag,nullAble);
        }
        else {
          semanticRegistry.putEntityEntryAtrNullable(entityTag, tag, nullAble);
        }

    }
    ///---------------------------------------------------------------------

    public void putTagAtrSizeMin(DaxTag entityTag, DaxTag tag, int min) {
        if (entityTag == null){
            semanticRegistry.putTagAtrSizeMin(tag,min);
        }
        else {
            semanticRegistry.putEntityEntryAtrSizeMin(entityTag,tag,min);
        }

    }
    ///---------------------------------------------------------------------

    public void putTagAtrSizeMax(DaxTag entityTag, DaxTag tag, int max) {
        if (entityTag == null){
            semanticRegistry.putTagAtrSizeMax(tag,max);
        }
        else {
            semanticRegistry.putEntityEntryAtrSizeMax(entityTag,tag,max);
        }
    }
    ///---------------------------------------------------------------------

}
