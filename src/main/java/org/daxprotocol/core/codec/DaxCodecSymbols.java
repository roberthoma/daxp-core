package org.daxprotocol.core.codec;

public final class DaxCodecSymbols {
    private DaxCodecSymbols() {}

    /** key=value */
    public static final char EQUAL = '=';

    /** Pair separator on the WIRE (binary, non-printable). */
    public static final char PAIR_SEPARATOR = 0x0001; // SOH


    /** Pair separator for logs/tests (printable). */
    public static final char FIELD_SEP_DEBUG = '|';

    /** Separator between the message preamble and the body section. */
    public static final String PREAMBLE_SEPARATOR = "\n";

    /** End of preamble and/or message block (network-friendly). */
    public static final String LINE_END = "\r\n";
}