package org.daxprotocol.core.strategy;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxDictionaryPopulator;
import org.daxprotocol.core.model.DaxMessage;

public class DaxCoreStrategyImpl implements DaxCoreStrategy{

    DaxpConfig config;
    DaxDictionary dictionary;
    DaxDictionaryPopulator dictionaryPopulator;
   public DaxCoreStrategyImpl(DaxpConfig config, DaxDictionary dictionary, DaxDictionaryPopulator dictionaryPopulator){
       this.config = config;
       this.dictionary = dictionary;
       this.dictionaryPopulator = dictionaryPopulator;
   }

    @Override public void populateFromAnnotations(Class<?> clazz) {
        dictionaryPopulator.populateFromAnnotations(dictionary, clazz);
    }

    @Override public void populateFromMessage(DaxMessage message) {
        dictionaryPopulator.populateFromMessage(dictionary, message);
    }
}
