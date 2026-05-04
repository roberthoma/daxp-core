package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.tool.DaxCollectionTool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DaxBaseDictionary<T> {

    Map<T, Map<DaxTag, DaxPair<?>>> attributMap = new ConcurrentHashMap<>();

    public void putAttribute(T key, DaxTag atrTag,  DaxPair<?> atrValue){
        attributMap.merge(key, new ConcurrentHashMap<>(Map.of(atrTag, atrValue)),
                (eM, nM) ->
                        DaxCollectionTool.putAndReturnMap(eM, atrTag, atrValue));

    }
}
