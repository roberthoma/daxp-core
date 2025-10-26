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

public abstract class DaxNumber<T> extends DaxValue<T> {
    T stepSize;      // Smallest value by which the variable can be incremented or decremented
    T maxValue;      // Maximum value
    T minValue;      // Minimum value

    protected int unitId;
    int precision = 0 ;   //For Double

    public DaxNumber(int fieldId, T value) {
        super(fieldId, value);
    }
    public DaxNumber(int fieldId, T value, int precision) {
        super(fieldId, value, precision);
    }


    public T getStepSize() {
        return stepSize;
    }

    public T getMaxValue() {
        return maxValue;
    }

    public T getMinValue() {
        return minValue;
    }

    public int getPrecision() {
        return precision;
    }

    public void setStepSize(T stepSize) {
        this.stepSize = stepSize;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }


    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public abstract void setValueUp();

    public abstract void setValueDown();

    @Override public DaxValue<T> copy() {
        return null;
    }
}
