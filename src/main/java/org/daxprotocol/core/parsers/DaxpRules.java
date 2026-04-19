package org.daxprotocol.core.parsers;

import org.daxprotocol.core.application.DaxCoreConstants;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.model.tag.DaxTag;

public final class DaxpRules {

//    public int resolveImplicitContext(int tagId, int msgContextId, DaxConfig config) {
//        if (tagId < DaxCoreConstants.DAXP_MAX_TAG_ID) {
//            return DaxCoreConstants.DAXP_CONTEXT_ID;
//        }
//        return msgContextId;
//    }

    public boolean shouldWriteContextPrefix(int tagContextId, int msgContextId, DaxConfig config) {
        return tagContextId != DaxCoreConstants.DAXP_CONTEXT_ID
                && tagContextId != msgContextId;
    }

    public boolean shouldSkipBlankValue(String value) {
        return value == null || value.isBlank();
    }

    public boolean isMessageStart(DaxTag tag) {
        return tag.equals(DaxCoreTags.MSG_TYPE);
    }

    public boolean isMessageEnd(DaxTag tag) {
        return tag.equals(DaxCoreTags.CHECKSUM);
    }
}