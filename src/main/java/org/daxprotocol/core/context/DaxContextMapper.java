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

package org.daxprotocol.core.context;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.context.DaxContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe mapper between context symbols (e.g. "FIX", "CRM")
 * and internal numeric context IDs.
 *
 * Supports both predefined contexts and dynamic registration
 * when unknown symbols appear in incoming messages.
 */
public final class DaxContextMapper {

    // ---------------------------------------
    // Maps
    // ---------------------------------------

    /** Maps symbol -> contextId */
    private static final ConcurrentMap<String, Integer> symbolToId = new ConcurrentHashMap<>();

    /** Maps contextId -> symbol */
    private static final ConcurrentMap<Integer, String> idToSymbol = new ConcurrentHashMap<>();

    /** Generator used for assigning IDs to unknown contexts */
    private static final AtomicInteger nextContextId = new AtomicInteger( DaxpConfig.CONTEXT_DYNAMIC_START_ID ); // dynamic contexts start at 100

    // ---------------------------------------
    // Static initialization (predefined contexts)
    // ---------------------------------------

    static {
        registerPredefined(DaxpConfig.DAX_CONTEXT_SYMBOL,
                           DaxpConfig.DAX_CONTEXT_ID);
    }

    public static void registerPredefined(String symbol, int id) {
        symbolToId.put(symbol, id);
        idToSymbol.put(id, symbol);
    }

    // ---------------------------------------
    // API
    // ---------------------------------------

    /**
     * Returns the numeric context ID for a symbol.
     * If the symbol does not exist yet, a new ID is created.
     */
    public static int getContextId(String symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Context symbol cannot be null");
        }
        String normalized = symbol.toUpperCase();
        return symbolToId.computeIfAbsent(normalized, s -> {
            int newId = nextContextId.getAndIncrement();
            idToSymbol.put(newId, s);
            return newId;
        });
    }

    /**
     * Returns the symbol for a given context ID.
     * Returns null if the ID is unknown.
     */
    public static String getContextSymbol(int id) {
        return idToSymbol.get(id);
    }

    /**
     * Checks if symbol is already registered.
     */
    public static boolean isKnownSymbol(String symbol) {
        if (symbol == null) return false;
        return symbolToId.containsKey(symbol.toUpperCase());
    }

    /**
     * Returns true if ID is already registered.
     */
    public static boolean isKnownId(int id) {
        return idToSymbol.containsKey(id);
    }

    /**
     * Returns all known symbols (for debugging or UI)
     */
    public static ConcurrentMap<String, Integer> getAllMappings() {
        return new ConcurrentHashMap<>(symbolToId);
    }

    // Prevent instantiation
    private DaxContextMapper() {}
}
