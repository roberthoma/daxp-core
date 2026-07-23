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
        namespace.setId(DaxCoreConstants.DAXP_NAMESPACE_ID);
        namespace.setSymbol(DaxCoreConstants.DAXP_NAMESPACE_SYMBOL);
        namespace.setTagPrefix(DaxCoreConstants.DAXP_NAMESPACE_TAG_PREFIX);
        namespace.setDescription(DaxCoreConstants.DAXP_NAMESPACE_DESCRIPTION);
       return namespace;
    }

}
