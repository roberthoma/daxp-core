package org.daxprotocol.core.registries;

import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;

import static org.daxprotocol.core.application.DaxCoreTags.ATR_DATA_TYPE;

public class DaxSemanticInspector {

//    DaxBaseRegistry<DaxTag> tagAttributes = new DaxBaseRegistry<>();
    DaxSemanticRegistry semanticRegistry;

    public DaxSemanticInspector(DaxSemanticRegistry semanticRegistry) {
        this.semanticRegistry = semanticRegistry;
    }

    public DaxDataType getDataType(DaxTag tag) {


        var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);

        if (attributes == null) {
            return DaxDataType.NONE;
        }

        var attribute = attributes.get(ATR_DATA_TYPE);

        if (attribute == null || attribute.getDataTypeValue() == null) {
            return DaxDataType.NONE;
        }

        return attribute.getDataTypeValue();
    }

    public DaxDataType getKeyDataType(DaxTag tag) {
        var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);
        return attributes.get(DaxCoreTags.COLLECTION_KEY_DATA_TYPE).getDataTypeValue();
    }

    public DaxDataType getValueDataType(DaxTag tag) {
        var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);
        return attributes.get(DaxCoreTags.COLLECTION_VALUE_DATA_TYPE).getDataTypeValue();
    }

    public boolean hasKey(DaxTag tag) {
        var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);

        if( attributes.containsKey(DaxCoreTags.COLLECTION_HAS_KEY)){
            return attributes.get(DaxCoreTags.COLLECTION_HAS_KEY).getBooleanValue();
        }
        return false;

    }

    public boolean isAllowDuplicates(DaxTag tag) {
        var attributes = semanticRegistry.getTagAttributes().getAttributMap().get(tag);

        if( attributes.containsKey(DaxCoreTags.COLLECTION_ALLOW_DUPLICATES)){
           return attributes.get(DaxCoreTags.COLLECTION_ALLOW_DUPLICATES).getBooleanValue();
        }
        return false;

    }
}
