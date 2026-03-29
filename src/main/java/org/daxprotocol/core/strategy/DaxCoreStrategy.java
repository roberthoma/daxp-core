package org.daxprotocol.core.strategy;

import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.model.DaxMessage;

public interface DaxCoreStrategy {
    void populateFromAnnotations(Class<?> clazz);
  //  void populateFromMessage(DaxMessage message);
}
