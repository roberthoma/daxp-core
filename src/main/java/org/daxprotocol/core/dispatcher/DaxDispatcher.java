package org.daxprotocol.core.dispatcher;

import org.daxprotocol.core.factory.DaxPreambleFactory;
import org.daxprotocol.core.model.DaxFrame;
import org.daxprotocol.core.parsers.DaxFrameParser;

import java.util.Map;

public class DaxDispatcher {
    DaxHandlerRegistry handlerRegistry;
    DaxFrameParser frameParser;
    DaxPreambleFactory preambleFactory;
    public DaxDispatcher(DaxPreambleFactory preambleFactory){
        this.preambleFactory = preambleFactory;
    }

    public DaxFrame dispatchRequest(Map<String, String> params, String body) {
        DaxFrame reqFrame ;
        DaxFrame respFrame = new DaxFrame();


      //  if (body != null && !body.isEmpty()) {
            reqFrame = frameParser.parseFrame(body);
//        } else {
//            reqFrame = frameParser.parseFromMap(params);
//        }
        DaxFrame frameResp = new DaxFrame();
        frameResp.setPreamble(preambleFactory.createRespPreamble(reqFrame));

        handlerRegistry.executor(reqFrame, respFrame);
        return  respFrame;

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
                .body(response.toDaxString()); // Returns DAXP|V=v...|...99=...

    } catch (DAXPException e) {
        // Handle your DAXP-XXXX exceptions here!
        return ResponseEntity.status(400)
                .body(this.errorGenerator.buildError(e));
    }
}



//        @PostMapping("/post") // GetMapping
//        public ResponseEntity<String> postMessage(@RequestParam(required = false) Map<String, String> params,
//                @RequestBody(required = false) String body)
//        {
//            return msgDisposeExe(params, body);
//        }


        ////////////////////////////
        // IN SPRINg boot application
//        public ResponseEntity<String> dispose(Map<String, String> params, String body) {
//            try {
//                // 1. Logic: Decide if we parse the Body (DAXP Message) or Params
//                DaxMessage incoming = null;
//
//                if (body != null && !body.isEmpty()) {
//                    incoming = this.parser.parse(body); // Handles <SOH> or |
//                } else {
//                    incoming = this.parser.fromMap(params);  <<<<<<<<<
//                }
//
//                // 2. Logic: Find the Handler (The "Registered" method)
//                // You mentioned: "registered method by handler"
//                DaxResponse response = this.handlerRegistry.execute(incoming);
//
//                // 3. Logic: Return the response in DAXP format
//                return ResponseEntity.ok()
//                        .header("Content-Type", "text/plain; charset=UTF-8")
//                        .body(response.toDaxString()); // Returns DAXP|V=v...|...99=...
//
//            } catch (DAXPException e) {
//                // Handle your DAXP-XXXX exceptions here!
//                return ResponseEntity.status(400)
//                        .body(this.errorGenerator.buildError(e));
//            }
//        }
//
        ////////////
        */