package org.daxprotocol.core.factory;

import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.model.preamble.DaxPreamble;

public class DaxPreambleFactory {

    DaxConfig config;
    DaxPreambleCodec preambleCodec;
    public DaxPreambleFactory(DaxConfig config, DaxPreambleCodec preambleCodec){
        this.config = config;
        this.preambleCodec = preambleCodec;
    }

    public DaxPreamble createPreamble(){
        DaxPreamble preamble = new DaxPreamble(config.getDefaultEncoding());
        preamble.setContextId(config.getAppContextId());
        return preamble;
    }

}
