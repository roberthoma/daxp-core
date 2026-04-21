package org.daxprotocol.core.codec;

import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.trailer.DaxTrailer;


public class DaxFrameCodec {
    DaxConfig config;
    DaxPreambleCodec preambleCodec;
    DaxMessageCodec messageCodec;
    public DaxFrameCodec(DaxConfig config,
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
