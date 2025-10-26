package org.daxprotocol.core.attributes;


import org.daxprotocol.core.codec.DaxTag;

public class DaxArtFieldName extends DaxAttribute<String>{
    public DaxArtFieldName(String value) {
        super(DaxTag.FIELD_NAME, value);
    }
}
