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

public class DaxInteger extends DaxNumber<Integer> {

    public DaxInteger(int fieldId, Integer value) {
        super(fieldId, value);
        this.stepSize = 1;
    }

    @Override
    public DaxInteger copy() {
        DaxInteger par = new DaxInteger(this.getFieldId(), this.getValue());
        par.stepSize = this.stepSize;
        return  par;
    }

    public void setValueUp(){
        this.value += this.stepSize;
    }

    public void setValueDown(){
        this.value -= this.stepSize;
    }

}
