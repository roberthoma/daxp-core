package org.daxprotocol.core.model.tag;

public enum DaxTagDestiny {
    ENTITY,
    ENTITY_FIELD,   //Use for VALUE register, value has readOnly Attribute
    ENTITY_VALUE,
    ENTITY_METHOD,
    COLLECTION,
    COLLECTION_VALUE,
    TAG,
    MSG,
    CONTEXT,
    SCHEMA,
    UNKNOW;
}
