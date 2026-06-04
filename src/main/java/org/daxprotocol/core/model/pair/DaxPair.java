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
package org.daxprotocol.core.model.pair;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;

public abstract class DaxPair<T>{
    DaxTag tag;
    char operator;
    protected T value;

    public DaxPair(DaxTag tag, T value){
        this.tag = tag;
        this.value = value;
        this.operator = DaxCoreConstants.EQUAL;
    }

    public DaxPair(DaxTag tag, T value, char operator){
        this.tag = tag;
        this.value = value;
        this.operator = operator;
    }

    public Class<?> getClazz(){
        return value.getClass();
    };
    public T getValue(){
        return value;
    }

//Move to data type decode
    public String getStrValue() {
        if (value instanceof Boolean){
            return ((Boolean)value)? "Y" : "N";
        }

        if (value == null ) { return null;}
//        if (value == null ) { return "[>>>  NULL <<<]";}

        return value.toString();
    };

    public char getCharValue() {
        return value.toString().charAt(0);
    };

    public char getOperator(){
        return operator;
    }

    public void setValue(T value) {
        this.value = value;
    }

//    @Override
//    public String toString(){
//        return tag.toString() + DaxCoreConstants.EQUAL +getStrValue() ; //TODO  DaxCodecSymbol.PAIR_SEPARATOR;
//    }
//
    public Integer getIntegerValue() {
        return Integer.valueOf((String) value);
    }
//
//
    public Boolean getBooleanValue() {
        if( value instanceof Character) {
            return (Character) value == 'Y' ? Boolean.TRUE : Boolean.FALSE;
        }
        if( value instanceof String) {
            return value.equals("Y") ? Boolean.TRUE : Boolean.FALSE;
        }
        return (Boolean) value;
    }

    public DaxDataType getDataTypeValue() {
        return (DaxDataType) value;
    }
//
public DaxTag getTag() {
    return tag;
}

}


/*


public <T> T getValue(DaxTag tag, Class<T> type) {
    DaxValue<?> wrappedValue = tagPairMap.get(tag);
    if (wrappedValue == null) return null;

    // Tutaj możesz sprawdzić spójność z tagDataType
    return type.cast(wrappedValue.getValue());
}
*/
