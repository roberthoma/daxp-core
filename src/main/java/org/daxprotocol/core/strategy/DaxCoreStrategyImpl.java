package org.daxprotocol.core.strategy;

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.populator.DaxPopulator;

public class DaxCoreStrategyImpl implements DaxCoreStrategy{

    DaxConfig config;
    DaxDictionary dictionary;
    DaxPopulator dictionaryPopulator;
   public DaxCoreStrategyImpl(DaxConfig config, DaxDictionary dictionary, DaxPopulator dictionaryPopulator){
       this.config = config;
       this.dictionary = dictionary;
       this.dictionaryPopulator = dictionaryPopulator;
   }

    @Override public void populateFromAnnotations(Class<?> clazz) {
        dictionaryPopulator.populateFromAnnotations(dictionary, clazz);
    }

    //TODO Add preamble or context , allow list of messages
//    @Override public void populateFromMessage(DaxMessage message) {
//
//        dictionaryPopulator.populateFromMessage(dictionary, message);
//    }
}
