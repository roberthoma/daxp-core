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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents the preamble of a DAXP message — defines
 * how the rest of the message is encoded and parsed.
 */
public class DaxPreamble {
    private String protocolVersion = "1";       // DAXP=1
    private DaxTagFormat tagFormat;       // TF=DEC
    private DaxEncoding encoding;         // EN=UTF8
    private String context;               // CTX=RxModeler or GUI, optional

    public DaxPreamble(){
        this.tagFormat = DaxTagFormat.DEC;
        encoding = DaxEncoding.UTF8;
    }



    public String getProtocolVersion() { return protocolVersion; }
    public DaxTagFormat getTagFormat() { return tagFormat; }
    public DaxEncoding getEncoding() { return encoding; }
    public String getContext() { return context; }

    public DaxPreamble setProtocolVersion(String version) { this.protocolVersion = version; return this; }
    public DaxPreamble setTagFormat(DaxTagFormat fmt) { this.tagFormat = fmt; return this; }
    public DaxPreamble setEncoding(DaxEncoding enc) { this.encoding = enc; return this; }
    public DaxPreamble setContext(String ctx) { this.context = ctx; return this; }

    public Map<String,String> toMap() {
        Map<String,String> map = new LinkedHashMap<>();
        map.put(DaxPreambleTag.DAXP.tag(), protocolVersion);
        map.put(DaxPreambleTag.TF.tag(), tagFormat.name());
        map.put(DaxPreambleTag.EN.tag(), encoding.name());
        if (context != null && !context.isEmpty())
            map.put(DaxPreambleTag.CTX.tag(), context);
        return map;
    }

    public static DaxPreamble fromMap(Map<String,String> map) {
        DaxPreamble p = new DaxPreamble();
        p.protocolVersion = map.getOrDefault("DAXP", "1");
        p.tagFormat = DaxTagFormat.valueOf(map.getOrDefault("TF", "DEC"));
        p.encoding = DaxEncoding.valueOf(map.getOrDefault("EN", "UTF8"));
        p.context = map.get("CTX");
        return p;
    }
}