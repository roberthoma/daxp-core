/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
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
package org.daxprotocol.core.codec;


import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.exceptions.DaxFrameParserException;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.preamble.DaxPreambleTag;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.daxprotocol.core.application.DaxCoreConstants.OPERATOR_EQUAL;

/**
 * Encodes and decodes the PREAMBLE section of a DAXP message.
 * Format example: DAXP|V=v0.0.1|E=UTF-8\n
 */
//public class DaxPreambleCodec implements DaxCodec<DaxPreamble> {
public class DaxPreambleCodec {

    DaxNamespaceMapper namespaceMapper;

    public DaxPreambleCodec( DaxNamespaceMapper namespaceMapper) {
        this.namespaceMapper = namespaceMapper;
    }

    private  void encode(StringBuilder sb, String tag, String value , char pairSeparator) {
        if (value.isBlank()){
            return ;
        }
        sb.append(tag)
                .append(OPERATOR_EQUAL)
                .append(value)
                .append(pairSeparator);
    }



    /** Encode Preamble object → wire format (string). */
    public String encode(DaxPreamble preamble) {
        Map<String,String> map = new LinkedHashMap<>();
        map.put(DaxPreambleTag.ENCODING.getTag(), preamble.getEncoding().getCanonicalName());

        map.put(DaxPreambleTag.MSG_NAMESPACE.getTag(),namespaceMapper.getReference(preamble.getnamespaceId()));

        if (preamble.getMsgCnt() > 1){
            map.put(DaxPreambleTag.MSG_QUANTITY.getTag(), String.valueOf(preamble.getMsgCnt()));
        }


        StringBuilder sb = new StringBuilder();
        sb.append(DaxCoreConstants.DAXP_NAMESPACE_SYMBOL)
          .append(preamble.getPairSeparator());
        encode(sb, DaxPreambleTag.VERSION.getTag(), preamble.getProtocolVersion() ,preamble.getPairSeparator()); //Always first

        map.forEach((k, v) -> encode(sb,k,v,preamble.getPairSeparator()));
        return sb.toString();
    }


    public boolean isTagPreamble(String tagStr){
        return  DaxPreambleTag.contains(tagStr);
    }

    public void decodeTag(String tagStr, String valueStr ,DaxPreamble preamble) {
        if (!DaxPreambleTag.contains(tagStr)) {
           throw new DaxFrameParserException("It "+tagStr+ " NOT  preamble tag !!!");
        }
            DaxPreambleTag tag = DaxPreambleTag.fromTag(tagStr);
            switch (tag) {
                case VERSION      -> preamble.setProtocolVersion(valueStr);
                case ENCODING    -> DaxCharacterEncoding.fromName(valueStr).ifPresent(preamble::setEncoding);
                case MSG_QUANTITY   -> preamble.setMsgCnt(Integer.parseInt(valueStr));
                case MSG_NAMESPACE -> preamble.setnamespaceId(namespaceMapper.getReferenceId(valueStr));
                //case MSG_SENDER  -> preamble.setSe System.out.println("Sender: " + value);
            }
        }


}
