package org.daxprotocol.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DaxpConfig {

    private static final String PROP_FILE = "application.properties"; // or "daxp.properties"
    private static final String DEFAULT_CONTEXT_KEY = "daxp.default-context-id";
    private static final int DEFAULT_CONTEXT_FALLBACK = 0;

    private final int defaultContextId;

    private DaxpConfig(int defaultContextId) {
        this.defaultContextId = defaultContextId;
    }

    public int getDefaultContextId() {
        return defaultContextId;
    }

    public static DaxpConfig loadFromClasspath() {
        Properties props = new Properties();

        try (InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(PROP_FILE)) {

            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            // optionally log, but don't crash the app
        }

        String value = props.getProperty(DEFAULT_CONTEXT_KEY);
        int ctx = DEFAULT_CONTEXT_FALLBACK;
        if (value != null) {
            try {
                ctx = Integer.parseInt(value.trim());
            } catch (NumberFormatException ignored) {
                // keep fallback
            }
        }
        return new DaxpConfig(ctx);
    }
}