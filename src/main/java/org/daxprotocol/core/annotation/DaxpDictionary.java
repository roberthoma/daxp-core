package org.daxprotocol.core.annotation;

public interface DaxpDictionary<K, V> {


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