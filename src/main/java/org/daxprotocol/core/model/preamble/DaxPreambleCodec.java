/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 Robert Homa
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***********************************************************************
 */
package org.daxprotocol.core.model.preamble;


import org.daxprotocol.core.codec.DaxCodec;
import org.daxprotocol.core.codec.DaxPairCodec;

import java.util.LinkedHashMap;
import java.util.Map;
import static org.daxprotocol.core.codec.DaxCodecSymbols.*;

/**
 * Encodes and decodes the PREAMBLE section of a DAXP message.
 * Format example: DAXP=1|TF=DEC|E=UTF8\n
 */
public class DaxPreambleCodec implements DaxCodec<DaxPreamble> {
    /** Encode Preamble object → wire format (string). */
    public String encode(DaxPreamble preamble) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(DaxPreambleTag.DAXP.tag(), preamble.getProtocolVersion());
        map.put(DaxPreambleTag.TF.tag() , preamble.getTagFormat().name());
        map.put( DaxPreambleTag.EN.tag(), preamble.getEncoding().name());
        if (preamble.getContext() != null && !preamble.getContext().isEmpty()) {
            map.put(DaxPreambleTag.CTX.tag(),  preamble.getContext());
        }

        StringBuilder sb = new StringBuilder();
        map.forEach((k, v) -> DaxPairCodec.encode(sb,k,v));
        sb.append(PREAMBLE_SEPARATOR);
        return sb.toString();
    }

    /** Decode wire format → Preamble object. */
    public DaxPreamble decode(String wire) {
        String line = wire.strip();
        if (line.endsWith(PREAMBLE_SEPARATOR)) {
            line = line.substring(0, line.length() - PREAMBLE_SEPARATOR.length());
        }

        Map<String, String> map = new LinkedHashMap<>();
        for (String pair : line.split(String.valueOf(PAIR_SEPARATOR))) {
            if (pair.isEmpty()) continue;
            int idx = pair.indexOf(EQUAL);
            if (idx < 0) continue;
            String tag = pair.substring(0, idx);
            String val = pair.substring(idx + 1);
            map.put(tag, val);
        }

        return DaxPreamble.fromMap(map);
    }
}
