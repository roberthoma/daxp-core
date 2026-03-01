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

package org.daxprotocol.core.mapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe mapper between  symbols (e.g. "FIX", "CRM") , group(Customer, Contract, ) , Enums
 * and internal numeric  IDs.
 *
 * Supports both predefined references and dynamic registration
 * when unknown symbols appear in incoming messages.
 */
public class DaxReferenceMapper <T>{

    // ---------------------------------------
    // Maps
    // ---------------------------------------

    /** Maps reference -> Id */
    private  final ConcurrentMap<T, Integer> referenceToId = new ConcurrentHashMap<>();

    /** Maps id -> reference */
    private  final ConcurrentMap<Integer, T> idToReference = new ConcurrentHashMap<>();

    /** Generator used for assigning IDs to unknown key */
    private  final AtomicInteger beginReferenceId;


    // ---------------------------------------
    // Static initialization (predefined reference)
    // ---------------------------------------
    int beginId;
    public DaxReferenceMapper(int beginId){
       this.beginId = beginId;
       beginReferenceId = new AtomicInteger( beginId );
    }

    public  void registerPredefined(DaxReference<?> reference) {

        //reference.getClass().equals()

        if (reference.getId() >= beginId){
            throw new RuntimeException("It is impossible to register predefined reference with ID "
                                         + reference.getId() + " greater than " + beginId);
        }
        registerPredefined( (T) reference.getReference(),reference.getId()) ;
    }

    public  void registerPredefined(T reference, int id) {
        referenceToId.put(reference, id);
        idToReference.put(id, reference);
    }

    // ---------------------------------------
    // API
    // ---------------------------------------
    /** Generator used for assigning IDs to unknown reference :   AtomicInteger referenceId */

    /**
     * Returns the numeric ID for a reference.
     * If the reference does not exist yet, a new ID is created.
     */
    public  int getReferenceId(T reference) {
        if (reference == null) {
            throw new IllegalArgumentException("Reference cannot be null");
        }
    //    String normalized = reference.toUpperCase();
        return referenceToId.computeIfAbsent(reference, s -> {
            int newId = beginReferenceId.getAndIncrement();
            idToReference.put(newId, s);
            return newId;
        });
    }

    /**
     * Returns the symbol for a given reference ID.
     * Returns null if the ID is unknown.
     */
    public  T getReference(int id) {
        return idToReference.get(id);
    }

    /**
     * Checks if symbol is already registered.
     */
    public  boolean isKnownSymbol(T reference) {
        if (reference == null) return false;

//        return referenceToId.containsKey(symbol.toUpperCase());
        return referenceToId.containsKey(reference);
    }

    /**
     * Returns true if ID is already registered.
     */
    public  boolean isKnownId(int id) {
        return idToReference.containsKey(id);
    }

    /**
     * Returns all known symbols (for debugging or UI)
     */
    public  ConcurrentMap<T, Integer> getAllMappings() {
        return new ConcurrentHashMap<>(referenceToId);
    }

}
