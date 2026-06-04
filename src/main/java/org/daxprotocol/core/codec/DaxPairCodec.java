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
package org.daxprotocol.core.codec;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPairCodec {
    DaxTagCodec tagCodec;
    public DaxPairCodec(  DaxTagCodec tagCodec) {
        this.tagCodec = tagCodec;
    }
    public   String encode(StringBuilder sb, DaxTag tag, String strValue , char pairSeparator, char operator) {

        if (strValue == null || strValue.trim().isBlank())
        {
            sb.append(tagCodec.encode(DaxCoreTags.VALUE_IS_NULL))
                    .append(operator)
                    .append(tagCodec.encode(tag))
                    .append(pairSeparator);
        }
        else {
            sb.append(tagCodec.encode(tag))
                    .append(operator)
                    .append(strValue)
                    .append(pairSeparator);
        }

        return sb.toString() ;
    }

    public   String encode(StringBuilder sb, DaxTag tag, String strValue , char pairSeparator) {
        return encode(sb, tag, strValue , pairSeparator, DaxCoreConstants.EQUAL);
    }


    public   String encode(StringBuilder sb, DaxPair<?> daxPair, char pairSeparator ) {
        String value;
        if (daxPair.getValue() instanceof DaxTag){
            value = tagCodec.encode((DaxTag) daxPair.getValue());
            return encode(sb,daxPair.getTag(),value ,pairSeparator , daxPair.getOperator());
        }
        if (daxPair.getValue() instanceof DaxDataType){
            value =  daxPair.getDataTypeValue().getCode();
            return encode(sb,daxPair.getTag(),value ,pairSeparator , daxPair.getOperator());

        }

        value = daxPair.getStrValue();
        return encode(sb,daxPair.getTag(),value ,pairSeparator, daxPair.getOperator() );
    }
}
