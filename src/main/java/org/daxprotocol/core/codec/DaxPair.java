/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 Robert Homa
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

//public abstract class DaxPair<T>{
public  class DaxPair<T>{
    Integer tag;
    protected T value;
    Class<T> clazz;

    public Class<?> getClazz(){
        return clazz;
    };
    public Integer getTag(){
        return tag;
    }
    public T getValue(){
        return value;
    }

    public DaxPair(Integer tag, T value){
        this.tag = tag;
        this.value = value;
        //this.clazz = value.getClass(); //TODO fix init class type
    }

    public String getStrValue() {
         return value.toString();
    };

    public void setValue(T value) {
        this.value = value;
    }

}
