/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***********************************************************************
 */

package org.daxprotocol.core.config;

import org.daxprotocol.core.context.DaxContext;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

public class DaxpConfigFactory {
    private static final String APPLICATION_CONTEXT_KEY = "daxp.application-context";
    private static final int APPLICATION_CONTEXT_FALLBACK = 0;

    private static final Pattern CONTEXT_PATTERN =
            Pattern.compile("^daxp\\.contexts\\[(\\d+)]\\.(\\w+)$");

    private static ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = DaxpConfigFactory.class.getClassLoader();
        }
        return cl;
    }

    public static Properties createProperties(String propertiesFile){

        Properties props = new Properties();
        try (InputStream is = getClassLoader().getResourceAsStream(propertiesFile)) {
            if (is == null) {
                throw new IllegalStateException("Config file not found on classpath: " + propertiesFile);
            }
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties: " + propertiesFile, e);
        }
        return props;
    }



    public static DaxpConfig createConfig(Properties props){
        DaxpConfig config = new DaxpConfig();
        String group;
        String parDomain = "org.daxprotocol.core.config.";

        group = "context.";
        String ctxTagPrefix   = props.getProperty(parDomain+group+"tag_prefix");
        String ctxSymbol      = props.getProperty(parDomain+group+"symbol");
        String ctxDescription = props.getProperty(parDomain+group+"description");

        String default_encoding = props.getProperty(parDomain+ "default_encoding");


        // Optional: validate
        if (ctxTagPrefix == null || ctxSymbol == null || ctxDescription == null) {
            throw new IllegalStateException("Missing "+parDomain+".* properties");
        }

        config.setAppContextTagPrefix(ctxTagPrefix);
        config.setAppContextSymbol(ctxSymbol);
        config.setAppContextDescription(ctxDescription);

//todo add defaultEncoding
        // >>>>>>> diffrent application can work in the same context


        return config;
    }

}


/***
 *
 org.daxprotocol.core.config.defaultEncoding="UTF-8"
 org.daxprotocol.core.config.tagPrefix=CRN
 org.daxprotocol.core.config.context.symbol=CUSTOMER
 org.daxprotocol.core.config.context.description=Customer data

 */
