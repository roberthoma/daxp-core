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

public class DaxString extends DaxValue<String> {
    int minLong = 0;
    int maxLong = 0;

    public int getMinLong() {
        return minLong;
    }

    public void setMinLong(int minLong) {
        this.minLong = minLong;
    }

    public int getMaxLong() {
        return maxLong;
    }

    public void setMaxLong(int maxLong) {
        this.maxLong = maxLong;
    }

    public DaxString(int fieldId ) {
        super(fieldId,"");
    }

    public DaxString(int fieldId , String value) {
        super(fieldId, value);
    }

    @Override
    public DaxString copy() {
        return new DaxString(this.fieldId,this.value);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    @Override
    public String toString(){
        return value;
    }

}
