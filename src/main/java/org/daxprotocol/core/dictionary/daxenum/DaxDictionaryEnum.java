package org.daxprotocol.core.dictionary.daxenum;

import org.daxprotocol.core.tool.DaxTool;

import java.util.HashMap;
import java.util.Map;

public class DaxDictionaryEnum {

    /*****************************************************
     *  Standard EnumMap
     * Map of string values and description ; enums others dictionary
     * Key : idField
     * */

    Map<String, DaxEnumName> enumMap = new HashMap<>();


    /*****************************************************
     *  Standard valueNamesMap
     * Map of string values and description ; enums others dictionary
     * Key : idField
     * */

    Map<String, Map<String, DaxEnumValue>> enumValueMap = new HashMap<>();



    public void putEnum(String name,  String desc){
        enumMap.computeIfAbsent(name,nameS -> new DaxEnumName(nameS, desc));
    }


    public void putEnumValue(String enumName, String value, String desc){
        enumValueMap.merge(enumName,new HashMap<>(Map.of(value, new DaxEnumValue(value , desc))),
                (svMap, svMapN)
                        ->  DaxTool.putAndReturn(svMap,value, svMapN.get(value)));
    }

    public Map<String, Map<String, DaxEnumValue>> getValueMap() {
        return  enumValueMap;
    }

    public  Map<String, DaxEnumName> getEnumMap(){
        return enumMap;
    }


}
