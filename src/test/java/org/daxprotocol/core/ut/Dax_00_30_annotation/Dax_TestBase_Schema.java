package org.daxprotocol.core.ut.Dax_00_30_annotation;

import org.daxprotocol.core.annotation.DaxpSchema;
import org.daxprotocol.core.annotation.DaxpTag;

@DaxpSchema
public class Dax_TestBase_Schema {


    @DaxpTag( uiLabel = "Ui Test TAG int", clazz = Integer.class)
    public static final int TEST_TAG_int            = 2001;

    @DaxpTag( uiLabel = "Ui Test TAG String", clazz = String.class)
    public static final int TEST_TAG_String            = 2002;

    public String TEST_TAG_char_uiLabel = "Ui Test TAG char";
    @DaxpTag( uiLabel = "Ui Test TAG char", clazz = Character.class)
    public static final int TEST_TAG_char            = 2003;



}
