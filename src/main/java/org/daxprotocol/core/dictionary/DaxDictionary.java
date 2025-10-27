package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.attributes.DaxAtrDataType;
import org.daxprotocol.core.attributes.DaxAtrUiLabel;
import org.daxprotocol.core.codec.DaxPair;

import java.util.*;

import static org.daxprotocol.core.codec.DaxTag.*;
public class DaxDictionary {
    /**
     * DescriptiveMap : it is main dic of describes fields / variables
     * Key : idField
     * */
    Map<Integer, Map<Integer, DaxPair<?>>> fieldAttrMap = new HashMap<>();

    public Map<Integer, DaxPair<?>> getFieldAttributeMap(int fieldId) {
        return fieldAttrMap.get(fieldId);
    }
    public Map<Integer, Map<Integer, DaxPair<?>>> getAttrMap(){
      return   fieldAttrMap;
    }


    /** Standard valueNamesMap
     * Map of One char values
     * Key : idField
     * */


    Map<Integer, Map<Character,String>> valueNamesMap = new HashMap<>();
    public Map<Integer, Map<Character, String>> getCharacterValueNameMap() {
        return valueNamesMap;
    }


    public void put(int fieldId, int unitId, String uiLabel, String desc, Class<?> clazz, boolean isUiEditable){
        Map<Integer, DaxPair<?>> attrMap = new HashMap<>();
        attrMap.put(ATR_UI_LABEL , new DaxAtrUiLabel(uiLabel));
        attrMap.put(FIELD_DATA_TYPE , new DaxAtrDataType(DaxAtrDataType.classToChar(clazz)));
        fieldAttrMap.put(fieldId,attrMap);
    };

    public void put(int fieldId, int unitId, String uiLabel, String desc, Class<?> clazz){
        put(fieldId, unitId,  uiLabel,  desc, clazz,false);
    };

    public void put(int fieldId, String uiLabel, String desc, Class<?> clazz){
        put(fieldId, 0,  uiLabel,  desc, clazz,false);
    };

    public void put(int fieldId,  String uiLabel, String desc){
        put(fieldId, 0,  uiLabel,  desc, Object.class,false);
    };

    public void put(int fieldId,  String uiLabel){
        put(fieldId, 0,  uiLabel, null, Object.class,false);
    };

    public void put(int fieldId,  String uiLabel, Class<?> clazz){
        put(fieldId, 0,  uiLabel,  null, clazz,false);
//        put(fieldId, 0,  uiLabel,  "No description :(", clazz,false);
    };

//    public void put(int fieldId, Map<Integer, DaxAttribute<?>> map){
//
//        if (!fieldAttrMap.containsKey(fieldId)){
//
//        }
//        fieldAttrMap.get()
//    }





    public void putValue(int idField, char charValue, String desc){

        if (!valueNamesMap.containsKey(idField)){
            Map<Character,String> valMap = new HashMap<>();
            valMap.put(charValue,desc);
            valueNamesMap.put(idField, valMap);
            return;
        }

        valueNamesMap.get(idField).put(charValue, desc);

    }
    //----------------------------------------------------
    public void join (DaxDictionary dic){
//TODO walidacja czy istniejący już idField czasami się nie dubluje.

  //      attributeMap.putAll (dic.getAttributeMap());
        valueNamesMap.putAll(dic.getCharacterValueNameMap());
    }


}
