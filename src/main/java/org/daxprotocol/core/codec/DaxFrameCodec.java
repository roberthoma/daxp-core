package org.daxprotocol.core.codec;

import org.daxprotocol.core.namespace.DaxNamespaceConfig;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.preamble.DaxPreamble;


public class DaxFrameCodec {
    DaxNamespaceConfig config;
    DaxPreambleCodec preambleCodec;
    DaxMessageCodec messageCodec;
    public DaxFrameCodec(DaxNamespaceConfig config,
                    DaxPreambleCodec preambleCodec,
                    DaxMessageCodec messageCodec)
    {
        this.config = config;
        this.preambleCodec = preambleCodec;
        this.messageCodec = messageCodec;

    }

    public String encode(DaxFrame frame){
        StringBuilder sb = new StringBuilder();
        DaxPreamble preamble = frame.getPreamble();
        sb.append(preambleCodec.encode(preamble));

        frame.getAllMessage().forEach(msg -> sb.append(messageCodec.encode(msg, preamble)));


        return sb.toString();
    }

}
