package org.daxprotocol.core.test.dax_02_use_case_tests.dax_00_20_base_parser;

import org.daxprotocol.core.codec.DaxPreambleCodec;
import org.daxprotocol.core.encoding.DaxCharacterEncoding;
import org.daxprotocol.core.exceptions.DaxFrameParserException;
import org.daxprotocol.core.mapper.DaxNamespaceMapper;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.parsers.DaxPreambleParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DaxPreambleParserTest {
    private final DaxNamespaceMapper mapper = new DaxNamespaceMapper();
    private final DaxPreambleParser parser = new DaxPreambleParser(new DaxPreambleCodec(mapper));

    @Test
    void parsesPreambleFieldsWithoutHeaderOrBraces() {
        DaxPreamble preamble = parser.parse("V=v1.2.3|EN=UTF-16|NS=FIX|MQ=1|PS=0x7C");
        assertEquals("V1.2.3", preamble.getProtocolVersion());
        assertEquals(DaxCharacterEncoding.UTF_16, preamble.getEncoding());
        assertEquals(mapper.getReferenceId("FIX"), preamble.getNamespaceId());
        assertEquals(1, preamble.getMsgCnt());
        assertEquals('|', preamble.getPairSeparator());
    }

    @Test
    void supportsWhitespaceAndOptionalTrailingSeparator() {
        assertEquals("V1.2.3", parser.parse(" V =v1.2.3 |EN=UTF-8|").getProtocolVersion());
    }

    @Test
    void separatorSettingDoesNotChangePreambleDelimiter() {
        DaxPreamble preamble = parser.parse("PS=0x01|MQ=2");
        assertEquals('\u0001', preamble.getPairSeparator());
        assertEquals(2, preamble.getMsgCnt());
    }

    @Test
    void rejectsMalformedFields() {
        String[] invalid = {null, "", " ", "V", "V=v1|EN", "V=v1||MQ=1",
                "UNKNOWN=1", "MQ=abc", "PS=xyz", "DAXP{V=v1}"};
        for (String input : invalid) {
            assertThrows(DaxFrameParserException.class, () -> parser.parse(input), input);
        }
    }
}
