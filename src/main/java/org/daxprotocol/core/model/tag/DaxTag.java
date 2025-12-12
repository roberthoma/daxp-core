package org.daxprotocol.core.model.tag;

import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.context.DaxContextMapper;

import java.util.Objects;

public class DaxTag {
    int contextId;
    int tagId;

    public int getContextId() {
        return contextId;
    }

    public void setContextId(int contextId) {
        this.contextId = contextId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public DaxTag(Integer tag) {
        this.contextId = DaxpConfig.APP_CONTEXT_ID;
        this.tagId = tag;
    }
    public DaxTag(int contextId, int tagId) {
        this.contextId = contextId;
        this.tagId = tagId;
    }
    public static DaxTag newPredefineTag(Integer tag) {
        return new DaxTag(DaxpConfig.DAX_CONTEXT_ID ,tag);
    }

    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DaxTag daxTag = (DaxTag) o;
        return contextId == daxTag.contextId && tagId == daxTag.tagId;
    }

    public boolean equals(Integer i) {
        return contextId == 0 && tagId == i;
    }

    @Override public int hashCode() {
        return Objects.hash(contextId, tagId);
    }

    @Override
    public String toString(){
//        return DaxContextMapper.getContextSymbol(contextId) + ":" + tagId;

//        if ( contextId != DaxpConfig.DAX_CONTEXT_ID &&
//
//        DaxContextMapper.getContextSymbol()

    return contextId != DaxpConfig.DAX_CONTEXT_ID ? contextId + ":" + tagId
                                                      : ""+tagId;

    }

}
