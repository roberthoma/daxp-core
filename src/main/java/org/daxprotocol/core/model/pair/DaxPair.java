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

import org.daxprotocol.core.codec.DaxCodecSymbol;
import org.daxprotocol.core.model.tag.DaxTag;

public  class DaxPair<T>{
    DaxTag tag;
    protected T value;

    public DaxPair(Integer tagId, T value){
        this.tag = new DaxTag(tagId);
        this.value = value;
    }
    public DaxPair(DaxTag tag, T value){
        this.tag = tag;
        this.value = value;
    }

    public Class<?> getClazz(){
        return value.getClass();
    };
    public DaxTag getTag(){
        return tag;
    }
    public T getValue(){
        return value;
    }


    public String getStrValue() {
        if (value instanceof Boolean){
            return ((Boolean)value)? "Y":"N";
        }
        return value.toString();
    };

    public void setValue(T value) {
        this.value = value;
    }
    @Override
    public String toString(){
        return tag.toString() + DaxCodecSymbol.EQUAL +getStrValue() ; //TODO  DaxCodecSymbol.PAIR_SEPARATOR;
    }

}
