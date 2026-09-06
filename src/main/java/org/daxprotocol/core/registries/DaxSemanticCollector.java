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

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxSemanticCollector {

    DaxSemanticRegistry semanticRegistry;
    DaxDataTypeService dataTypeService;
    public DaxSemanticCollector(DaxSemanticRegistry semanticRegistry,
                                DaxDataTypeService dataTypeService ){
        this.semanticRegistry = semanticRegistry;
        this.dataTypeService = dataTypeService;
    }


    // Critical exception (type mismatch) during semantic registration at application startup stops the application
    // During registration via message: log with a warning or an error

private void registerTagDataType( DaxTag tag,DaxDataType daxDataType ){
 //   if (semanticRegistry.isTagRegistered(tag)){
        DaxDataType existDataType = semanticRegistry.getTagDataType(tag);

        if (existDataType == DaxDataType.UNKNOWN){
            semanticRegistry.putTagAtrDataType(tag,daxDataType);
        }
        else {
            if (existDataType != daxDataType){
                throw new RuntimeException("TAG EXIST WITH DATA TYPE "+ existDataType.getCode()+
                        " NEW:"+daxDataType.getCode());
            }

        }

//    }
//    else {
//        semanticRegistry.putTagAtrDataType(tag,daxDataType);
//    }


}

    public void registerTag(DaxTag tag,
              Class<?> tagClass,
            String description
    ){
        registerTag(tag,
                dataTypeService.decodeClass(tagClass),
                 description);

    }


    public void registerTag(DaxTag tag,
                            DaxDataType daxDataType,
                            String description
    )

    {

        semanticRegistry.putTagAtrDescription(tag, description);

        registerTagDataType(tag, daxDataType);


    }

    public void registerTag(DaxTag tag,
            DaxTag entityTag,
            DaxDataType daxDataType,
            String description
    )

    {

        if (semanticRegistry.isTagRegistered(tag)){
            DaxDataType existDataType = semanticRegistry.getTagDataType(tag);
            if (existDataType == DaxDataType.UNKNOWN){ // || existDataType == DaxDataType.NONE){
                semanticRegistry.putTagAtrDataType(tag,daxDataType);
            }
        }
        else {
            semanticRegistry.putTagAtrDataType(tag,daxDataType);
        }



    }


}
