package org.daxprotocol.core.annotation;

import java.util.HashMap;
import java.util.Map;

public interface DaxpDictionaryEntry<K, V> {


        K key();

        V value();

        String symbol();

        default String description() {
            return "";
        }

//        default String dictionaryName() {
//            return this.getClass().getSimpleName();
//        }

       // Map<K,V> getMap();

    }