package org.daxprotocol.core.codec;

import org.daxprotocol.core.model.DaxMessage;

public interface DaxCodec {
    String encode(DaxMessage message);
    DaxMessage decode(String wire);
}