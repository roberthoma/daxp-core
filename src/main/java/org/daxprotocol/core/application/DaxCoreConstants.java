package org.daxprotocol.core.application;

public class DaxCoreConstants {
    /*****************************************************
     *  DAXP namespace
     */
    public static final int    DAXP_NAMESPACE_ID          = 0;
    public static final String DAXP_NAMESPACE_SYMBOL      = "DAXP";
    public static final String DAXP_NAMESPACE_TAG_PREFIX  = "$" ;
    public static final String DAXP_SYMBOL                = "DAXP";
    public static final String DAXP_NAMESPACE_DESCRIPTION = "Data & Attribute eXchange Protocol";
    /*****************************************************
    *  Operators
    *
    * */
    public static final char OPERATOR_EQUAL     = '=';
    public static final char OPERATOR_REFERENCE = '@';
    public static final char OPERATOR_ACTION    = '^';


    /*****************************************************
     *                Main Separator
     */
    /**  Pair separator on the WIRE (binary, non-printable). */
    public static char   DEFAULT_PAIR_SEPARATOR = 0x0001;  //PS

    /*****************************************************
     *                Preamble Separators
     */
    public static char   PREAMBLE_OPENING_BRACE  = '{';  // 0x7B Opening brace
    public static char   PREAMBLE_CLOSING_BRACE  = '}';  // 0x7D Closing brace
    public static char   PREAMBLE_PAIR_SEPARATOR = '|';  // 0x7C Vertical bar

    public static final char NAMESPACE_TAG_SEPARATOR = ':';

    public static final char TAG_LIST_SEPARATOR_CHAR     = ';';
    public static final CharSequence TAG_LIST_SEPARATOR  = ";";

    public static final char DECIMAL_SEPARATOR    = '.';

    public static final char ESCAPE_CHAR = '\\';

    /****************************************************
    *     BULK collection separators
    *      HEX value
    */
    public static char SEPARATOR_FILE          = 0x001C; //	<FS> File Separator
    public static char SEPARATOR_GROUP         = 0x001D; // <GS> Group Separator
    public static char SEPARATOR_RECORD        = 0x001E; // <RS> Record Separator
    public static char SEPARATOR_UNIT          = 0x001F; //	<US> Unit Separator
    public static char END_OF_MEDIUM           = 0x0019; //	<EM> End of medium


    /*****************************************************
     *     DAXP  mappers
     */
    public static final int  START_IDX_MSG_MAPPER    = 101;
    public static final int  START_IDX_CTX_MAPPER    = 101;
    public static final int  START_IDX_SCHEMA_MAPPER = 101;


    /******************************************************
     *  Operations
     */

    public static final String  OPERATION_NULL = "N";

}
