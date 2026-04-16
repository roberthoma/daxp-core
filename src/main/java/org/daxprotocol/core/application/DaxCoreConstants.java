package org.daxprotocol.core.application;

public class DaxCoreConstants {
    /*****************************************************
     *  DAXP Context
     */
    public static final int    DAXP_CONTEXT_ID          = 0;
    public static final String DAXP_CONTEXT_SYMBOL      = "DAXP";
    public static final String DAXP_SYMBOL              = "DAXP";
    public static final String DAXP_CONTEXT_DESCRIPTION = "Data & Attribute eXchange Protocol";
    public static final char TAG_LIST_SEPARATOR_CHAR    = ';';
    //public static final CharSequence VALUE_LIST_SEPARATOR    = ";";
//    public static final CharSequence CONTEXT_TAG_SEPARATOR = ":";
    public static final char CONTEXT_TAG_SEPARATOR = ':';
    /** key=value */
    public static final char EQUAL = '=';


    /*****************************************************
     * Separators
     */
//    public static final char TAG_LIST_SEPARATOR    = ';';
//    public static final char VALUE_LIST_SEPARATOR  = ';';
//    public static final char CONTEXT_TAG_SEPARATOR = ',';

    public static final CharSequence TAG_LIST_SEPARATOR    = ";";
    public static final char CONTEXT_TAG_SEPARATOR_CHAR = ':';
    /*****************************************************
     *  Reserved tags for DAXP
     */
    public static final int    DAXP_MAX_TAG_ID = 999;
    public static final String DAXP_CONTEXT_TAG_PREFIX  = "$" ;
    public static final int  START_IDX_MSG_MAPPER = 101;
    public static final int  START_IDX_CTX_MAPPER = 101;
    /**  Pair separator on the WIRE (binary, non-printable). */
    public static char PAIR_SEPARATOR = 0x0001;
}
