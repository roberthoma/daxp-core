package org.daxprotocol.core.unit_test.dax_10_00_annotation;

import org.daxprotocol.core.annotation.DaxpManifest;
import org.daxprotocol.core.annotation.DaxpNamespace;

@DaxpManifest
public class DaxAnyManifest {

//todo powiązać z przykładowym application_BASE.properties


    @DaxpNamespace(name = "utNamespace" ,description = "Namespace Ut")
    public static final String NAMESPACE_TEST =   "NS_UT";    // ALIAS

}
