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
    // 1) read application-context-id
    public int getApplicationContext() {
        String ctxIdStr = props.getProperty("daxp.application-context-id");
        if (ctxIdStr == null || ctxIdStr.isBlank()) {
            throw new IllegalStateException("Missing property: daxp.application-context-id");
        }
        try {
            return Integer.parseInt(ctxIdStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Invalid integer for daxp.application-context-id: " + ctxIdStr, e);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
   //-------------------------------------------------------------
   // 2) read contexts[*]
   public Map<Integer,DaxContext> getContextMap(){
        Map<Integer, Map<String, String>> grouped = new TreeMap<>();

        for (String key : props.stringPropertyNames()) {
            Matcher m = CONTEXT_PATTERN.matcher(key);
            if (m.matches()) {
                int index = Integer.parseInt(m.group(1));
                String field = m.group(2);
                String value = props.getProperty(key);

                grouped.computeIfAbsent(index, x -> new HashMap<>())
                       .put(field, value);
            }
        }

        Map<Integer,DaxContext> contextsMap = new HashMap<>();

        for (Map.Entry<Integer, Map<String, String>> entry : grouped.entrySet()) {
            Map<String, String> fields = entry.getValue();

            DaxContext ctx = new DaxContext();

            // id is required
            String idStr = fields.get("id");
            if (idStr == null) {
                throw new IllegalStateException("Missing 'id' for daxp.contexts[" + entry.getKey() + "]");
            }
            try {
                ctx.id = Integer.parseInt(idStr.trim());
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Invalid 'id' for daxp.contexts[" + entry.getKey() + "]: " + idStr, e);
            }

            ctx.tagPrefix   = fields.getOrDefault("tagPrefix", "");
            ctx.symbol      = fields.getOrDefault("symbol", "");
            ctx.description = fields.getOrDefault("description", "");

            contextsMap.put(ctx.id,ctx);
        }
        return contextsMap;
    }

    //-------------------------------------------------------------
    // Read daxp.tag-format
    public String getTagFormat() {
        String tagFormatStr = props.getProperty("daxp.tag-format");
        if (tagFormatStr == null || tagFormatStr.isBlank()) {
            throw new IllegalStateException("Missing property: daxp.tag-format");
        }
        return tagFormatStr;
    }
    //-------------------------------------------------------------
    // Read daxp.pair-separator
    public char getPairSeparator() {
        String value = props.getProperty("daxp.pair-separator");

        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing property: daxp.pair-separator");
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

        throw new IllegalArgumentException("Invalid pair separator value: " + value);
    }

}
