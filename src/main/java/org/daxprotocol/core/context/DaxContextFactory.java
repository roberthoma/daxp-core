package org.daxprotocol.core.context;

import org.daxprotocol.core.config.DaxpConfig;

public class DaxContextFactory {

    public static DaxContext createAppContext(DaxpConfig config){
        DaxContext context = new DaxContext();
        context.setId(config.getAppContextId());
        context.setSymbol(config.getAppContextSymbol());
        context.setTagPrefix(config.getAppContextTagPrefix());
        context.setDescription(config.getAppContextDescription());
       return context;
    }

    public static DaxContext createSysContext(){
        DaxContext context = new DaxContext();
        context.setId(DaxpConfig.DAXP_CONTEXT_ID);
        context.setSymbol(DaxpConfig.DAXP_CONTEXT_SYMBOL);
        context.setTagPrefix("");
        context.setDescription(DaxpConfig.DAXP_CONTEXT_DESCRIPTION);
       return context;
    }

}
