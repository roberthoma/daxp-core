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

public class DaxBoolean extends DaxValue<Boolean> {
    public DaxBoolean(int fieldId, Boolean value) {
        super(fieldId, value);
    }
    public DaxBoolean(int fieldId, String strValue) {
        super(fieldId,null);
        setValue(strValue);
    }
    public DaxBoolean(int fieldId,  int value) {
        super(fieldId,null);
        setValue(value);
    }

    @Override
    public DaxBoolean copy() {
        return new DaxBoolean(this.fieldId, this.value);
    }

    public void switchValue(){
        this.value = !this.value;
    }



    public void setValue(boolean b) {
        this.value = b ;
    }

    public void setValue(String strValue) {


        if( strValue != null &&
                (strValue.isEmpty() || strValue.isBlank())
        )
        {
            this.value = Boolean.FALSE;
            return;
        }

        this.value =  strValue != null
                && (strValue.toUpperCase().compareTo("ON") == 0
                || strValue.toUpperCase().compareTo("TRUE") == 0
                || strValue.toUpperCase().compareTo("YES") == 0
                || strValue.toUpperCase().compareTo("Y") == 0
                || strValue.compareTo("1") == 0
        );
    }

    public void setValue(int intValue) {
        this.value = intValue > 0;
    }

    public void setValue(char intValue) {
        this.value = intValue == '1' ||
                intValue == 'T' || intValue == 't' ||
                intValue == 'Y' || intValue == 'y' ;
    }

}
