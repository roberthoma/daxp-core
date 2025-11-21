package org.daxprotocol.core.dictionary;

import java.util.HashMap;
import java.util.Map;

public class DaxContextDIc {


    String defaultContext;


    Map<String , DaxContext>  contextMap = new HashMap<>();


    Map<String,DaxDictionary> dictionaryMap =  new HashMap<>();



    public DaxContext getContext(String symbol) {
        return contextMap.get(symbol);
    }

    public DaxDictionary getDictionary(String contextSymbol) {
        return dictionaryMap.get(contextSymbol);
    }


    public DaxDictionary getDefaultDic() {
        return dictionaryMap.get(defaultContext);
    }





}
