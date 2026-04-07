package org.daxprotocol.core.dispatcher;

import org.daxprotocol.core.codec.DaxMessageCodec;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.parsers.DaxParser;

import java.util.List;
import java.util.Map;

public class DaxDispatcher {
    DaxHandlerRegistry handlerRegistry;
    DaxParser parser;

    public DaxFrame dispatchRequest(Map<String, String> params, String body) {
        DaxFrame frame ;
        List<DaxMessage> incoming  = null;
        DaxPreamble preamble = null;

        if (body != null && !body.isEmpty()) {
            frame = parser.parseFromString(body);
        } else {
            frame = parser.parseFromMap(params);
        }

        return handlerRegistry.executor(frame);


    }

    public DaxFrame dispatchRequest(Map<String, String> params){
        return dispatchRequest(params, null);
    }

    public DaxFrame dispatchRequest(String body){
        return dispatchRequest(null, body);
    }
}

/*
public ResponseEntity<String> dispose(Map<String, String> params, String body) {
    try {
        // 1. Logic: Decide if we parse the Body (DAXP Message) or Params
        DaxMessage incoming = null;

        if (body != null && !body.isEmpty()) {
            incoming = this.parser.parse(body); // Handles <SOH> or |
        } else {
            incoming = this.parser.fromMap(params);
        }

        // 2. Logic: Find the Handler (The "Registered" method)
        // You mentioned: "registered method by handler"
        DaxResponse response = this.handlerRegistry.execute(incoming);

        // 3. Logic: Return the response in DAXP format
        return ResponseEntity.ok()
                .header("Content-Type", "text/plain; charset=UTF-8")
                .body(response.toDaxString()); // Returns DAXP=v...|...99=...

    } catch (DAXPException e) {
        // Handle your DAXP-XXXX exceptions here!
        return ResponseEntity.status(400)
                .body(this.errorGenerator.buildError(e));
    }
}
        */