package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpNamespace;
import org.daxprotocol.core.annotation.DaxpModel;

@DaxpNamespace
public class DaxAnyManifest {


    @DaxpModel(name = "utModel" ,description = "Model Ut")
    public static final String MODEL_BASE_UT =   "Model_UT";

}
