package org.daxprotocol.core.parsers;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.exceptions.DaxFrameParserException;
import org.daxprotocol.core.model.preamble.DaxPreamble;

import java.util.Objects;

public class DaxPreambleParser {
    private final DaxPreambleCodec preambleCodec;

    public DaxPreambleParser(DaxPreambleCodec preambleCodec) {
        this.preambleCodec = Objects.requireNonNull(preambleCodec);
    }

    /**
     * Parses preamble fields, for example {@code V=v1.2.3|EN=UTF-16|NS=FIX|MQ=1|PS=0x7C}.
     * The input excludes the DAXP header and braces; a trailing separator is optional.
     */
    public DaxPreamble parse(String preambleStr) {
        if (preambleStr == null || preambleStr.isBlank()) {
            throw new DaxFrameParserException("Preamble is EMPTY!");
        }

        DaxPreamble preamble = new DaxPreamble();
        int position = 0;
        while (position < preambleStr.length()) {
            int next = preambleStr.indexOf(DaxCoreConstants.PREAMBLE_PAIR_SEPARATOR, position);
            if (next == -1) {
                next = preambleStr.length();
            }
            int operator = preambleStr.indexOf(DaxCoreConstants.OPERATOR_EQUAL, position);
            if (operator == -1 || operator >= next) {
                throw new DaxFrameParserException("Missing '=' in preamble field!");
            }

            String tag = preambleStr.substring(position, operator).trim();
            String value = preambleStr.substring(operator + 1, next);
            try {
                preambleCodec.decodeTag(tag, value, preamble);
            } catch (DaxFrameParserException e) {
                throw e;
            } catch (Exception e) {
                throw new DaxFrameParserException("Error parsing preamble tag: " + tag, e);
            }
            position = next + 1;
        }
        return preamble;
    }
}
