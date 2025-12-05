package org.daxprotocol.core.config;

import org.daxprotocol.core.model.context.DaxContext;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DaxpPropertiesLoader {


    private static final String PROP_FILE = "application.properties"; // or "daxp.properties"
    private static final String APPLICATION_CONTEXT_KEY = "daxp.application-context-id";
    private static final int APPLICATION_CONTEXT_FALLBACK = 0;


    // daxp.contexts[0].id
    private static final Pattern CONTEXT_PATTERN =
            Pattern.compile("^daxp\\.contexts\\[(\\d+)]\\.(\\w+)$");

    String propertiesFile;
    Properties props;
    public DaxpPropertiesLoader(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    private static ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = DaxpPropertiesLoader.class.getClassLoader();
        }
        return cl;
    }


    //-------------------------------------------------------------
    public void load() {
        props = new Properties();
        try (InputStream is = getClassLoader().getResourceAsStream(propertiesFile)) {
            if (is == null) {
                throw new IllegalStateException("Config file not found on classpath: " + propertiesFile);
            }
            props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties: " + propertiesFile, e);
        }
    }
   //-------------------------------------------------------------
   //  read application context
   public DaxContext getApplicationContext() {

       String prefix      = props.getProperty("daxp.contexts.tagPrefix");
       String symbol      = props.getProperty("daxp.contexts.symbol");
       String description = props.getProperty("daxp.contexts.description");

       // Optional: validate
       if (prefix == null || symbol == null || description == null) {
           throw new IllegalStateException("Missing daxp.contexts.* properties");
       }

       DaxContext context = new DaxContext();
       context.tagPrefix = prefix;
       context.symbol = symbol;
       context.description = description;

       return context;
   }
    //-------------------------------------------------------------
    private String getStringValue(String property) {
        String valueStr = props.getProperty(property);
        if (valueStr == null || valueStr.isBlank()) {
            throw new IllegalStateException("Missing property: "+valueStr);
        }
        return valueStr;
    }


    //-------------------------------------------------------------
    // Read daxp.encoding
    public String getEncoding() {
        return getStringValue("daxp.encoding");
    }
    //-------------------------------------------------------------

    private char getCharValue(String property) {
        String value = props.getProperty(property);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing property: "+property);
        }

        value = value.trim();

        // Case 1: hex format: 0x0001, 0x1F, etc.
        if (value.startsWith("0x") || value.startsWith("0X")) {
            try {
                int code = Integer.parseInt(value.substring(2), 16);
                return (char) code;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid hex char: " + value);
            }
        }

        // Case 2: quoted char: '|' or '#'
        if (value.length() >= 3 && value.startsWith("'") && value.endsWith("'")) {
            return value.charAt(1);
        }

        // Case 3: direct single character: |
        if (value.length() == 1) {
            return value.charAt(0);
        }

        throw new IllegalArgumentException("Invalid "+property+" value: " + value);
    }
    //-------------------------------------------------------------
    // Read daxp.pair-separator,
    //      daxp.context-tag-separator

    public char getPairSeparator() {
        return getCharValue("daxp.pair-separator");
    }

}
