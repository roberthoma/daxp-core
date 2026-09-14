package org.daxprotocol.core.test.dax_02_use_case_tests.dax_010_00_annotation;

import org.daxprotocol.core.annotation.DaxpManifest;
import org.daxprotocol.core.annotation.DaxpNamespace;

@DaxpManifest
public class DaxAnyManifest {

//todo powiązać z przykładowym application_BASE.properties


    @DaxpNamespace(name = "utNamespace" ,description = "Namespace Ut")
    public static final String NAMESPACE_TEST =   "NS_UT";    // ALIAS

}
