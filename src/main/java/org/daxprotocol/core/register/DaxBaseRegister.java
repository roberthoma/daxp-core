package org.daxprotocol.core.register;

import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.tool.DaxCollectionTool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DaxBaseRegister {
    /*****************************************************
     * Main SET of tags
     */
    Set<DaxTag> tagSet = new HashSet<>();

    Map<DaxTag, Map<DaxTag, DaxPair<?>>> attributMap = new ConcurrentHashMap<>();

    public void putAttribute(DaxTag tag, DaxTag atrTag,  DaxPair<?> atrValue){
        attributMap.merge(tag, new ConcurrentHashMap<>(Map.of(atrTag, atrValue)),
                (eM, nM) ->
                        DaxCollectionTool.putAndReturnMap(eM, atrTag, atrValue));

    }
}
