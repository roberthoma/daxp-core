package org.daxprotocol.core.factory;

import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.namespace.DaxNamespaceConfig;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.preamble.DaxPreamble;

public class DaxPreambleFactory {

    DaxNamespaceConfig config;
    DaxPreambleCodec preambleCodec;
    public DaxPreambleFactory(DaxNamespaceConfig config, DaxPreambleCodec preambleCodec){
        this.config = config;
        this.preambleCodec = preambleCodec;
    }

    public DaxPreamble createPreamble(){
        DaxPreamble preamble = new DaxPreamble(config.getDefaultEncoding());
        preamble.setNamespaceId(config.getAppNamespaceId());
        return preamble;
    }

    public DaxPreamble createRespPreamble(DaxFrame frameReq) {
        DaxPreamble preamble = new DaxPreamble(config.getDefaultEncoding());
        preamble.setNamespaceId(config.getAppNamespaceId());
        preamble.setPairSeparator(frameReq.getPreamble().getPairSeparator());
        return preamble;
    }
}
