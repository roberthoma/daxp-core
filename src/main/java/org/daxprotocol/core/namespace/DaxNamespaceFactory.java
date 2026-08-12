package org.daxprotocol.core.namespace;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.config.DaxConfig;

public class DaxNamespaceFactory {

    public static DaxNamespace createAppNamespace(DaxConfig config){
        DaxNamespace namespace = new DaxNamespace();
        namespace.setId(config.getAppNamespaceId());
        namespace.setSymbol(config.getAppNamespaceSymbol());
        namespace.setTagPrefix(config.getAppNamespaceTagPrefix());
        namespace.setDescription(config.getAppNamespaceDescription());
       return namespace;
    }

    public static DaxNamespace createSysNamespace(){
        DaxNamespace namespace = new DaxNamespace();
        namespace.setId(DaxCoreConstants.DAXP_NAMESPACE_ID);
        namespace.setSymbol(DaxCoreConstants.DAXP_NAMESPACE_SYMBOL);
        namespace.setTagPrefix(DaxCoreConstants.DAXP_NAMESPACE_TAG_PREFIX);
        namespace.setDescription(DaxCoreConstants.DAXP_NAMESPACE_DESCRIPTION);
       return namespace;
    }

}
