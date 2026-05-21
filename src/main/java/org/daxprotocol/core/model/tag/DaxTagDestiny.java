package org.daxprotocol.core.model.tag;

public enum DaxTagDestiny {
    ENTITY,
    ENTITY_FIELD,   //Use for VALUE register, value has readOnly Attribute
    ENTITY_VALUE,
    COLLECTION,
    COLLECTION_VALUE,
    TAG,
    UNKNOW;
}
