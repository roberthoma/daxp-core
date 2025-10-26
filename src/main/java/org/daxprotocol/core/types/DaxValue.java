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

package org.daxprotocol.core.types;

import java.util.Set;

public abstract class DaxValue<T>  {

    protected int fieldId;    //Fix protocol FIELD id
  //  protected int unitId;
    protected T value;
    protected T defaultValue;        // Default value if not initialized
    Set<T> allowedValues;       //TODO Developing Set or MAP of available values and  add to dictionary
    //boolean  isGuiEditable; do zastanowienia
    //RxParameterNode node;

    public DaxValue() {
        this.value = null;
        this.defaultValue = null;
    }

    public DaxValue(int fieldId, T value, T defaultValue, int unitId) {
        this.value   = value;
        this.fieldId = fieldId;
        this.defaultValue = defaultValue;
    }


    public DaxValue(int fieldId, T value, int unitId) {
        this( fieldId, value, value, unitId);
    }


    public DaxValue(int fieldId, T value) {
        this( fieldId, value, value, 0);
    }

    public void reset(){
        value = defaultValue;
    }

    public abstract DaxValue<T> copy();
    public Class<?> getValueClass(){
        return value!=null ? value.getClass() : null;
    }

    public String toString(){
        return fieldId+"="+value;
    }

    public T getValue(){
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getFieldId() {
        return fieldId;
    }
}
