package org.daxprotocol.core.codec;

import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.body.DaxBodyCodec;
import org.daxprotocol.core.model.head.DaxHeadCodec;
import org.daxprotocol.core.model.preamble.DaxPreambleCodec;
import org.daxprotocol.core.model.trailer.DaxTrailerCodec;

public class DaxMessageCodec implements DaxCodec<DaxMessage>{
    DaxPreambleCodec preambleCodec = new DaxPreambleCodec();
    DaxHeadCodec headCodec = new DaxHeadCodec();
    DaxBodyCodec bodyCodec = new DaxBodyCodec();
    DaxTrailerCodec trailerCodec = new DaxTrailerCodec();



    @Override public String encode(DaxMessage message) {
        StringBuilder sb = new StringBuilder();

        sb.append(preambleCodec.encode(message.getPreamble()))
          .append(headCodec.encode(message.getHead(), message.getBody().getBlockCount()))
          .append(bodyCodec.encode(message.getBody()))
          .append(trailerCodec.encode(message.getTrailer()));

        return sb.toString();
    }

    @Override public DaxMessage decode(String wire) {
        return null;
    }
}
