package org.daxprotocol.core.test.dax_01_unit_test.dax_00_07_base_codec;
import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxPairCodec;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.pair.DaxPairTagSet;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DaxPairCodecTest {

    @Mock
    private DaxTagCodec tagCodec;

    @Mock
    private DaxTag mockTag;

    @Mock
    private DaxTag mockValueTag;

    @Mock
    private DaxPair<Object> mockPair;

    @Mock
    private DaxDataType mockDataType;

    private DaxPairCodec pairCodec;

    @BeforeEach
    void setUp() {
        pairCodec = new DaxPairCodec(tagCodec);
    }

    @Nested
    @DisplayName("Encoding Method Tests")
    class EncodeTests {

        @Test
        @DisplayName("encode(sb, tag, strValue, separator, operator) - Appends formatted string correctly")
        void testEncodeWithExplicitOperator() {
            when(tagCodec.encode(mockTag)).thenReturn("100");
            StringBuilder sb = new StringBuilder("PREFIX:");

            String result = pairCodec.encode(sb, mockTag, "VAL", ';', '=');

            assertEquals("PREFIX:100=VAL;", result);
            assertEquals("PREFIX:100=VAL;", sb.toString());
            verify(tagCodec, times(1)).encode(mockTag);
        }

        @Test
        @DisplayName("encode(sb, tag, strValue, separator) - Defaults to DaxCoreConstants.OPERATOR_EQUAL")
        void testEncodeWithDefaultOperator() {
            when(tagCodec.encode(mockTag)).thenReturn("200");
            StringBuilder sb = new StringBuilder();

            String result = pairCodec.encode(sb, mockTag, "TEST_VAL", '|');

            // Assumes OPERATOR_EQUAL equals '='
            assertEquals("200=TEST_VAL|", result);
            verify(tagCodec, times(1)).encode(mockTag);
        }

        @Test
        @DisplayName("encode(sb, daxPair, separator) - Value is instance of DaxTag")
        void testEncodePairWithDaxTagValue() {
            when(mockPair.getTag()).thenReturn(mockTag);
            when(mockPair.getValue()).thenReturn(mockValueTag);
            when(mockPair.getOperator()).thenReturn('=');

            when(tagCodec.encode(mockTag)).thenReturn("10");
            when(tagCodec.encode(mockValueTag)).thenReturn("99");

            StringBuilder sb = new StringBuilder();
            String result = pairCodec.encode(sb, mockPair, ';');

            assertEquals("10=99;", result);
            verify(tagCodec, times(1)).encode(mockTag);
            verify(tagCodec, times(1)).encode(mockValueTag);
        }

        @Test
        @DisplayName("encode(sb, daxPair, separator) - Value is instance of DaxDataType")
        void testEncodePairWithDaxDataTypeValue() {
            when(mockPair.getTag()).thenReturn(mockTag);
            when(mockPair.getValue()).thenReturn(mockDataType);
            when(mockPair.getDataTypeValue()).thenReturn(mockDataType);
            when(mockDataType.getCode()).thenReturn("INT_CODE");
            when(mockPair.getOperator()).thenReturn(':');

            when(tagCodec.encode(mockTag)).thenReturn("50");

            StringBuilder sb = new StringBuilder();
            String result = pairCodec.encode(sb, mockPair, ',');

            assertEquals("50:INT_CODE,", result);
            verify(tagCodec, times(1)).encode(mockTag);
            verify(mockDataType, times(1)).getCode();
        }

        @Test
        @DisplayName("encode(sb, daxPair, separator) - Standard String value fallback")
        void testEncodePairWithStandardStringValue() {
            when(mockPair.getTag()).thenReturn(mockTag);
            when(mockPair.getValue()).thenReturn("SimpleString");
            when(mockPair.getStrValue()).thenReturn("SimpleString");
            when(mockPair.getOperator()).thenReturn('=');

            when(tagCodec.encode(mockTag)).thenReturn("101");

            StringBuilder sb = new StringBuilder();
            String result = pairCodec.encode(sb, mockPair, '#');

            assertEquals("101=SimpleString#", result);
            verify(tagCodec, times(1)).encode(mockTag);
            verify(mockPair, times(1)).getStrValue();
        }
    }

    @Nested
    @DisplayName("Decoding Method Tests")
    class DecodeTests {

        @Test
        @DisplayName("decode() - When tag is REQ_FIELD_LIST, splits list and decodes tags into DaxPairTagSet")
        void testDecodeReqFieldListTag() {
            String valueStr = "TAG1"+ DaxCoreConstants.TAG_LIST_SEPARATOR+"TAG2"+DaxCoreConstants.TAG_LIST_SEPARATOR+"TAG3";
            int namespaceId = 1;

            DaxTag decodedTag1 = mock(DaxTag.class);
            DaxTag decodedTag2 = mock(DaxTag.class);
            DaxTag decodedTag3 = mock(DaxTag.class);

            when(tagCodec.decode("TAG1", namespaceId)).thenReturn(decodedTag1);
            when(tagCodec.decode("TAG2", namespaceId)).thenReturn(decodedTag2);
            when(tagCodec.decode("TAG3", namespaceId)).thenReturn(decodedTag3);

            DaxPair<?> resultPair = pairCodec.decode(DaxCoreTags.REQ_FIELD_LIST, valueStr, '=', namespaceId);
            assertInstanceOf(DaxPairTagSet.class, resultPair);
            assertEquals(DaxCoreTags.REQ_FIELD_LIST, resultPair.getTag());

            @SuppressWarnings("unchecked")
            Set<DaxTag> tagSet = (Set<DaxTag>) resultPair.getValue();
            assertEquals(3, tagSet.size());
            assertTrue(tagSet.contains(decodedTag1));
            assertTrue(tagSet.contains(decodedTag2));
            assertTrue(tagSet.contains(decodedTag3));

            verify(tagCodec, times(1)).decode("TAG1", namespaceId);
            verify(tagCodec, times(1)).decode("TAG2", namespaceId);
            verify(tagCodec, times(1)).decode("TAG3", namespaceId);
        }

        @Test
        @DisplayName("decode() - Standard tag returns DaxPairString")
        void testDecodeStandardTag() {
            int namespaceId = 2;
            char operator = '=';
            String valueStr = "SampleValue";

            DaxPair<?> resultPair = pairCodec.decode(mockTag, valueStr, operator, namespaceId);

            assertInstanceOf(DaxPairString.class, resultPair);
            assertEquals(mockTag, resultPair.getTag());
            assertEquals(valueStr, resultPair.getValue());
            assertEquals(operator, resultPair.getOperator());

            verifyNoInteractions(tagCodec);
        }
    }
}