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
package org.daxprotocol.core.model.value;

import org.daxprotocol.core.datatype.DaxDataType;

public abstract class DaxValue<T>{
    protected T value;

    public DaxValue( T value){
        this.value = value;
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
        return value.toString();
    };

    public char getCharValue() {
        return value.toString().charAt(0);
    };


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
}
