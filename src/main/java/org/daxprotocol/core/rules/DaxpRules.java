package org.daxprotocol.core.rules;

import org.daxprotocol.core.codec.DaxTagConst;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.model.tag.DaxTag;

public final class DaxpRules {

    public int resolveImplicitContext(int tagId, int msgContextId, DaxpConfig config) {
        if (tagId < DaxpConfig.DAXP_MAX_TAG_ID) {
            return DaxpConfig.DAXP_CONTEXT_ID;
        }
        return msgContextId;
    }

    public boolean shouldWriteContextPrefix(int tagContextId, int msgContextId, DaxpConfig config) {
        return tagContextId != DaxpConfig.DAXP_CONTEXT_ID
                && tagContextId != msgContextId;
    }

    public boolean shouldSkipBlankValue(String value) {
        return value == null || value.isBlank();
    }

    public boolean isMessageStart(DaxTag tag) {
        return tag.equals(DaxTagConst.MSG_TYPE);
    }

    public boolean isMessageEnd(DaxTag tag) {
        return tag.equals(DaxTagConst.CHECKSUM);
    }
}