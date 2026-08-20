/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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


package org.daxprotocol.core.tool;

import java.nio.charset.StandardCharsets;

public class DaxChecksumService {

    public static int calculateSum(String input){
        byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);
        int sum = 0;

        for (byte b : bytes) {
            sum += b;
        }
        return sum;

    }


    public static int calculateModulo(int sum){
        return sum % 256;
    }

    public  static int calculateChecksum(String input) {
        return calculateModulo(calculateSum(input));
    }

}
