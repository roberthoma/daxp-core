package org.daxprotocol.core.ut.Dax_00_00_base_config;

import org.daxprotocol.core.config.DaxpConfigFactory;
import org.daxprotocol.core.parsers.DaxParserService;
import org.daxprotocol.core.provider.DaxProvider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class DaxBaseTestConfig {
    public static DaxProvider provider;
    public static DaxParserService parser;
    public static int appContextId;

    @BeforeAll
    public static void initAll() {
        if (provider == null) {
            provider = new DaxProvider(DaxpConfigFactory
                    .createConfig(DaxpConfigFactory
                            .createProperties("application_BASE.properties")));

            parser = provider.getParserService();
            appContextId = provider.getConfig().getAppContextId();

            System.out.println("*******************************************");
            System.out.println("      Base Application Configuration  << ");
            System.out.println(" Description  = "+ provider.getConfig().getAppContextDescription());
            System.out.println(" Symbol       = "+ provider.getConfig().getAppContextSymbol());
            System.out.println(" Tag Prefix   = "+ provider.getConfig().getAppContextTagPrefix());
            System.out.println(" Context Id   = "+ provider.getConfig().getAppContextId());
            System.out.println("*******************************************");

        }
    }

    @Order(1)
    @Test
    void checkAppContextId(){
        Assertions.assertEquals(1, provider.getConfig().getAppContextId());
    }


}
