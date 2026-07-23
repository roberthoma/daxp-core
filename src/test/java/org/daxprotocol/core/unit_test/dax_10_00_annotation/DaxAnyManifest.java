package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpNamespace;
import org.daxprotocol.core.annotation.DaxpSchema;

@DaxpNamespace
public class DaxAnyManifest {


    @DaxpSchema(name = "utSchema" ,description = "Base Schema Ut")
    public static final String SCHEMA_BASE_UT =   "SHEMA_UT";

}
