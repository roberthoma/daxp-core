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

    public DaxPreamble getPreamble() {
        return preamble;
    }

    public DaxHead getHead() {
        return head;
    }

    public DaxBody getBody() {
        return body;
    }

    public DaxTrailer getTrailer() {
        return trailer;
    }

    public DaxMessage(String msgType) {
        this.preamble = new DaxPreamble();
        this.head = new DaxHead(msgType);
        this.body = new DaxBody();
        this.trailer = new DaxTrailer();
    }
//    public DaxMessage(DaxPreamble preamble, DaxHead head, DaxBody body, DaxTrailer trailer) {
//        this.preamble = preamble;
//        this.head = head;
//        this.body = body;
//        this.trailer = trailer;
//    }

    public String getType() {
        return head.getMsgType();
    }
}
