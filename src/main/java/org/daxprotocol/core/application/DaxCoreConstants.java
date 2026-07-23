package org.daxprotocol.core.application;

public class DaxCoreConstants {
    /*****************************************************
     *  DAXP namespace
     */
    public static final int DAXP_NAMESPACE_ID = 0;
    public static final String DAXP_NAMESPACE_SYMBOL = "DAXP";
    public static final String DAXP_NAMESPACE_TAG_PREFIX = "$" ;
    public static final String DAXP_SYMBOL              = "DAXP";
    public static final String DAXP_NAMESPACE_DESCRIPTION = "Data & Attribute eXchange Protocol";
    /*****************************************************
    *  Operators
    *
    * */
    public static final char OPERATOR_EQUAL = '=';
    public static final char OPERATOR_BLOCK_REFERENCE = '@';
    public static final char OPERATOR_ACTION = '^';


    /*****************************************************
     *                Separators
     */
    /**  Pair separator on the WIRE (binary, non-printable). */
    public static char DEFAULT_PAIR_SEPARATOR = 0x0001;
    public static char[] ALLOWED_PAIR_SEPARATORS = { DEFAULT_PAIR_SEPARATOR,'|','#'};

    public static final char namespace_TAG_SEPARATOR = ':';
    public static final CharSequence TAG_LIST_SEPARATOR    = ";";
    public static final char TAG_LIST_SEPARATOR_CHAR    = ';';

    public static final char DECIMAL_SEPARATOR    = '.';



//    public static final char TAG_LIST_SEPARATOR    = ';';
//    public static final char VALUE_LIST_SEPARATOR  = ';';
//    public static final char namespace_TAG_SEPARATOR = ',';
//    public static final CharSequence VALUE_LIST_SEPARATOR    = ";";
//    public static final CharSequence namespace_TAG_SEPARATOR = ":";


    /*****************************************************
     *     DAXP  mappers
     */
    public static final int  START_IDX_MSG_MAPPER    = 101;
    public static final int  START_IDX_CTX_MAPPER    = 101;
    public static final int  START_IDX_SCHEMA_MAPPER = 101;

}
