package org.daxprotocol.core.context;

import org.daxprotocol.core.config.DaxConfig;

public class DaxContextFactory {

    public static DaxContext createAppContext(DaxConfig config){
        DaxContext context = new DaxContext();
        context.setId(config.getAppContextId());
        context.setSymbol(config.getAppContextSymbol());
        context.setTagPrefix(config.getAppContextTagPrefix());
        context.setDescription(config.getAppContextDescription());
       return context;
    }

    public static DaxContext createSysContext(){
        DaxContext context = new DaxContext();
        context.setId(DaxConfig.DAXP_CONTEXT_ID);
        context.setSymbol(DaxConfig.DAXP_CONTEXT_SYMBOL);
        context.setTagPrefix(DaxConfig.DAXP_CONTEXT_TAG_PREFIX);
        context.setDescription(DaxConfig.DAXP_CONTEXT_DESCRIPTION);
       return context;
    }

}
