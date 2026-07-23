package org.daxprotocol.core.namespace;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.config.DaxConfig;

public class DaxNamespaceFactory {

    public static DaxNamespace createAppNamespace(DaxConfig config){
        DaxNamespace namespace = new DaxNamespace();
        namespace.setId(config.getAppnamespaceId());
        namespace.setSymbol(config.getAppnamespaceSymbol());
        namespace.setTagPrefix(config.getAppnamespaceTagPrefix());
        namespace.setDescription(config.getAppnamespaceDescription());
       return namespace;
    }

    public static DaxNamespace createSysNamespace(){
        DaxNamespace namespace = new DaxNamespace();
        namespace.setId(DaxCoreConstants.DAXP_namespace_ID);
        namespace.setSymbol(DaxCoreConstants.DAXP_namespace_SYMBOL);
        namespace.setTagPrefix(DaxCoreConstants.DAXP_namespace_TAG_PREFIX);
        namespace.setDescription(DaxCoreConstants.DAXP_namespace_DESCRIPTION);
       return namespace;
    }

}
