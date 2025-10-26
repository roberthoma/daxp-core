package org.daxprotocol.core.model.trailer;

import org.daxprotocol.core.codec.DaxCodec;
import static org.daxprotocol.core.codec.DaxTag.*;
import static org.daxprotocol.core.codec.DaxCodecSymbols.*;

public class DaxTrailerCodec implements DaxCodec<DaxTrailer> {
    @Override public String encode(DaxTrailer message) {
        StringBuilder sb = new StringBuilder();
        sb.append(CHECKSUM).append(EQUAL)
                .append("123")
                .append(PAIR_SEPARATOR);

        return sb.toString();
    }

    @Override public DaxTrailer decode(String wire) {
        return null;
    }
}
