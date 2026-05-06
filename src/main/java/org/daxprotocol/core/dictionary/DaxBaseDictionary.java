package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.tool.DaxCollectionTool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//TODO extend for : TAGS, FIELDS  , ENTITY, MESSAGE, SCHEMA,
//add universal Attribute like  : name, description, deprecated ...

public class DaxBaseDictionary<T> {

    Map<T, Map<DaxTag, DaxPair<?>>> attributMap = new ConcurrentHashMap<>();

    public void putAttribute(T key, DaxPair<?> atrPair){
        attributMap.merge(key, new ConcurrentHashMap<>(Map.of(atrPair.getTag(), atrPair)),
                (eM, nM) ->
                        DaxCollectionTool.putAndReturnMap(eM, atrPair.getTag(), atrPair));

    }

    public Map<T, Map<DaxTag, DaxPair<?>>> getAttributMap(){
        return attributMap;
    }


}
