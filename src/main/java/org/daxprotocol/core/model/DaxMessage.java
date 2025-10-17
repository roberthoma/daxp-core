package org.daxprotocol.core.model;

import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.head.DaxHead;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.trailer.DaxTrailer;

public final class DaxMessage {
    private final DaxPreamble preamble;
    private final DaxHead head;
    private final DaxBody body;
    private final DaxTrailer trailer;

    public DaxMessage(DaxPreamble preamble, DaxHead head, DaxBody body, DaxTrailer trailer) {
        this.preamble = preamble;
        this.head = head;
        this.body = body;
        this.trailer = trailer;
    }
}
