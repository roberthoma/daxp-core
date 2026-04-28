package org.daxprotocol.core.register;

import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.tag.DaxTag;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DaxBaseRegister {

    Map<DaxTag, Map<DaxTag, DaxPair<?>>> attributMap = new ConcurrentHashMap<>();

}
