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

import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;

import static org.daxprotocol.core.application.DaxCoreTags.ATR_DATA_TYPE;

public class DaxSemanticInspector {

//    DaxBaseRegistry<DaxTag> tagAttributes = new DaxBaseRegistry<>();
    DaxSemanticRegistry semanticRegistry;

    public DaxSemanticInspector(DaxSemanticRegistry semanticRegistry) {
        this.semanticRegistry = semanticRegistry;
    }

    public DaxDataType getDataType(DaxTag tag) {


        var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);

        if (attributes == null) {
            return DaxDataType.NONE;
        }

        var attribute = attributes.get(ATR_DATA_TYPE);

        if (attribute == null || attribute.getDataTypeValue() == null) {
            return DaxDataType.NONE;
        }

        return attribute.getDataTypeValue();
    }

    public DaxDataType getKeyDataType(DaxTag tag) {
        var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);
        return attributes.get(DaxCoreTags.COLLECTION_KEY_DATA_TYPE).getDataTypeValue();
    }

    public DaxDataType getCollectionValueDataType(DaxTag tag) {
        var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);
        return attributes.get(DaxCoreTags.COLLECTION_VALUE_DATA_TYPE).getDataTypeValue();
    }

    private boolean checkBooleanValue(DaxTag tag, DaxTag booleanValueTag){
            var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);

            if( attributes.containsKey(booleanValueTag)){
                return attributes.get(booleanValueTag).getBooleanValue();
            }
            return false;
    }

    public boolean hasKey(DaxTag tag) {
        return checkBooleanValue(tag, DaxCoreTags.COLLECTION_HAS_KEY);

    }

    public boolean isAllowDuplicates(DaxTag tag) {
        return checkBooleanValue(tag, DaxCoreTags.COLLECTION_ALLOW_DUPLICATES);
    }


    public boolean isDictionary(DaxTag tag) {
        return checkBooleanValue(tag, DaxCoreTags.COLLECTION_IS_DICTIONARY);
    }

    public boolean isCollectionClosed(DaxTag tag) {
        return checkBooleanValue(tag, DaxCoreTags.COLLECTION_IS_CLOSED);
    }
}
