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

package org.daxprotocol.core.unit_test.dax_00_00_service;

import org.daxprotocol.core.annotation.DaxpDTO;

import java.lang.reflect.Field;

public class DaxDecoratorService {


    public static  void printDaxGroupInfo(DaxpDTO group ){
        System.out.println(" >> DaxpGroup ");

        System.out.println("GRP name : " +group.name());
//        System.out.println("GRP id : " +group.groupId());
//        System.out.println("GRP master id : " +group.masterId());
        System.out.println("GRP desc : " +group.description());
       // System.out.println("GRP namespace : " +group.namespace());
    }

    public static void  printDaxFieldInfo(Field field){
        System.out.println("\n> POP FIELD Name : "+field.getName());
        System.out.println("___> POP FIELD Type Name      : "+field.getType().getTypeName());
        System.out.println("___> POP FIELD Component Type : "+field.getType().getComponentType());
    }

    public static void  printDaxEnumInfo(Field field){
        if (!field.getType().isEnum()){
            throw new RuntimeException("It ["+field.getName()+"] is NOT ENUM field !!!");
        }
        System.out.println("___> POP FIELD is ENUM");
        Object[] constants = field.getType().getEnumConstants();
        for (Object c : constants) {
            System.out.println(c);
        }
    }

    public static  void printDaxScanClass(Class<?> clazz){
        System.out.println("====================================");
        System.out.println("->  POP CLASS :"+clazz.getSimpleName());

    }

}
