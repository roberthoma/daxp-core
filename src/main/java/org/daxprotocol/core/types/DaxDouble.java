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

import org.apache.commons.math3.util.Precision;

public class DaxDouble extends DaxNumber<Double> {

    public DaxDouble(int fieldId, Double value, int precision) {
        super(fieldId, value);
        this.precision = precision;
    }
    public DaxDouble(int fieldId, Double value) {
        super(fieldId, value);
        this.precision = 0;
    }

    @Override
    public DaxDouble copy() {
        return new DaxDouble(this.fieldId, this.getValue(), this.precision);
    }

    public Double getValue() {
        return Precision.round(super.getValue(),precision);
    }

    public void setValueUp(){
        this.value += this.stepSize;
    }


    public void setValueDown(){
        this.value -= this.stepSize;
    }

}