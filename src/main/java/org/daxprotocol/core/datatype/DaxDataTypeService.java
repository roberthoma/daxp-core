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

package org.daxprotocol.core.datatype;

import org.daxprotocol.core.annotation.DaxpDictionary;
import org.daxprotocol.core.annotation.DaxpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class DaxDataTypeService {
    private static final Logger logger = LoggerFactory.getLogger(DaxDataTypeService.class);

    //--------------------------------------------------------------------------------------
    public DaxDataTypeService() {
    }
    //--------------------------------------------------------------------------------------

    public boolean isDaxpCollection(Object object){
        return decodeFromObject(object).equals(DaxDataType.COLLECTION) ;
    }
    //--------------------------------------------------------------------------------------

/*
    public boolean isDaxpCollection(Class<?> clazz) {


        if (clazz.equals(List.class)) return true;
        if (clazz.equals(Map.class)) return true;
        if (clazz.equals(Set.class)) return true;
        if (clazz.equals(Queue.class)) return true;
        if (clazz.equals(Collection.class)) return true;

      //  if (clazz.equals(Enum.class)) return true;    //TODO CHECK again  this is dictionary
        if (clazz.isEnum()) return true;              //TODO CHECK again  this is dictionary

        if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        }
        if (Map.class.isAssignableFrom(clazz)) {
            return true;
        }

        if (List.class.isAssignableFrom(clazz)) {
            return true;
        }

        return false;
    } */
    //--------------------------------------------------------------------------------------

    public boolean isDaxpCollection(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        return Collection.class.isAssignableFrom(clazz)
                || Map.class.isAssignableFrom(clazz)
                || clazz.isEnum();
    }
    //--------------------------------------------------------------------------------------
    public boolean isObjInstanceOfCollection(Object obj){
        if (obj instanceof List<?>) return true;
        if (obj instanceof Map<?,?>) return true;
        if (obj instanceof Set<?>) return true;
        if (obj instanceof Collection<?>) return true;

        return false;
    }
    //--------------------------------------------------------------------------------------
    public DaxDataType decodeFromObject(Object obj) {
        if (obj == null) return DaxDataType.NONE;

        if (obj instanceof Enum<?>) return DaxDataType.STRING;   //?????????

        return decodeClass(obj.getClass());
    }
    //--------------------------------------------------------------------------------------
    public    DaxDataType decodeClass(Type type) {
        return decodeClass(castReflectTypeToClass(type));
    }
    //--------------------------------------------------------------------------------------
    public    DaxDataType decodeClass(Class<?> clazz) {
        if (clazz == null)  return DaxDataType.NONE;

        if (clazz == String.class) return DaxDataType.STRING;

        if (clazz.equals(Integer.class)) return DaxDataType.INTEGER;
        if (clazz.equals(int.class)) return DaxDataType.INTEGER;

        if (clazz.equals(Long.class)) return DaxDataType.LONG;
        if (clazz.equals(long.class)) return DaxDataType.LONG;

        if (clazz.equals(BigDecimal.class)) return DaxDataType.DECIMAL;

        if (clazz.equals(Double.class)) return DaxDataType.DOUBLE;
        if (clazz.equals(double.class)) return DaxDataType.DOUBLE;

        if (clazz.equals(Boolean.class)) return DaxDataType.BOOLEAN;
        if (clazz.equals(boolean.class)) return DaxDataType.BOOLEAN;

        if (clazz.equals(Character.class)) return DaxDataType.CHARACTER;
        if (clazz.equals(char.class)) return DaxDataType.CHARACTER;

        if (clazz.equals(LocalDate.class)) return DaxDataType.LOCAL_DATE;
        if (clazz.equals(LocalDateTime.class)) return DaxDataType.LOCAL_DATE_TIME;

        if(isDaxpCollection(clazz)) return DaxDataType.COLLECTION;

        if (clazz.isAnnotationPresent(DaxpEntity.class)) {
            return DaxDataType.ENTITY;
        }

//        if (DaxpDictionary.class.isAssignableFrom(clazz)) {
//            return DaxDataType.COLLECTION;
//        }

        if (clazz.equals(Enum.class)) return DaxDataType.STRING;   //?????????

        return DaxDataType.UNKNOWN;
    }

    //--------------------------------------------------------------------------------------
    public  boolean isPrimitiveType(Class<?> clazz){
        if (clazz == String.class) return true;
        if (clazz.equals(Integer.class)) return true;
        if (clazz.equals(int.class)) return true ;
        if (clazz.equals(char.class)) return true;
        if (clazz.equals(Long.class)) return true;
        if (clazz.equals(BigDecimal.class)) return true;
        if (clazz.equals(Double.class))  return true;
        if (clazz.equals(Boolean.class)) return true;
        if (clazz.equals(LocalDate.class)) return true;
        if (clazz.equals(LocalDateTime.class)) return true;
        if (clazz.equals(Character.class)) return true;

        return false;
    }

    //--------------------------------------------------------------------------------------
    public  Class<?> castReflectTypeToClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        return null;
    }
    //--------------------------------------------------------------------------------------
    /*
     public DaxCollectionInfo getCollectionInfo(Class<?> clazz){
         DaxCollectionInfo info = new DaxCollectionInfo();

         if (clazz == Set.class){  // is collection
             info.isCollection = true;
         }

         if (clazz == List.class){
             info.isColAllowDuplicates = true;
//             info.isCollection = true;
         }

         if (clazz.isEnum() || clazz.equals(Enum.class)
         ){
             info.isCollection = true;
             info.isColHasKey = true;
             info.isColDictionary = true;
             info.isJavaEnum = true;
             info.isClosed = true;
         }

         if (clazz == Map.class
             || Map.class.isAssignableFrom(clazz)
             || clazz == HashMap.class
         ){
             info.isCollection = true;
             info.isColHasKey = true;
         }

         if (clazz == Queue.class){
             info.isCollection = true;
         }
         if (clazz == LinkedList.class){
             info.isCollection = true;
             info.isColNavigable = true;
         }


         if (Collection.class.isAssignableFrom(clazz)) {
             info.isCollection = true;  ///??? inmutable m
         }


         if (!info.isCollection){
             throw new RuntimeException("It is NOT COLLECTION !!!");
         }

         return info;
     }
*/
    public DaxCollectionInfo getCollectionInfo(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class parameter cannot be null");
        }

        DaxCollectionInfo info = new DaxCollectionInfo();

        // 1. Enums / Dictionaries
        if (clazz.isEnum() || Enum.class.isAssignableFrom(clazz)) {
            info.isCollection = true;
            info.isColHasKey = true;
            info.isColDictionary = true;
            info.isJavaEnum = true;
            info.isClosed = true;
            return info;
        }

        // 2. Maps (Key-Value Collections)
        if (Map.class.isAssignableFrom(clazz)) {
            info.isCollection = true;
            info.isColHasKey = true;

            if (NavigableMap.class.isAssignableFrom(clazz) || SortedMap.class.isAssignableFrom(clazz)) {
                info.isColNavigable = true;
            }
            return info;
        }

        // 3. Java Collections (Lists, Sets, Queues, Deques)
        if (Collection.class.isAssignableFrom(clazz)) {
            info.isCollection = true;

            // Allows duplicates: List, Queue, Deque allow duplicate elements
            if (List.class.isAssignableFrom(clazz) || Queue.class.isAssignableFrom(clazz)) {
                info.isColAllowDuplicates = true;
            }

            // Navigable collections: Deque, LinkedList, NavigableSet, SortedSet
            if (Deque.class.isAssignableFrom(clazz)
                    || NavigableSet.class.isAssignableFrom(clazz)
                    || SortedSet.class.isAssignableFrom(clazz)) {
                info.isColNavigable = true;
            }
        }

        // Guard Clause: Fail fast if class isn't a supported collection type
        if (!info.isCollection) {
            throw new IllegalArgumentException("Class [" + clazz.getName() + "] is NOT a supported collection or enum!");
        }

        return info;
    }
       //--------------------------------------------------------------------------------------
       public boolean isMap(Object object) {
           DaxCollectionInfo info = getCollectionInfo(object.getClass());

           return info.isColHasKey && !info.isJavaEnum;
       }

}
