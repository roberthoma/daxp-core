package org.daxprotocol.core.annotation;

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxAnnotationNote {


    DaxTag entityTag;
    DaxTag tag;
    DaxDataType daxDataType;
    String name = "";
    String description = "";
    Class<?> clazz = Void.class;
    DaxDataType keyDataType;
    DaxDataType valueDataType;
    boolean readOnly;

    public DaxTag getTag() {
        return tag;
    }

    public void setTag(DaxTag tag) {
        this.tag = tag;
    }


    public DaxDataType getDaxDataType() {
        return daxDataType;
    }

    public void setDaxDataType(DaxDataType daxDataType) {
        this.daxDataType = daxDataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public DaxDataType getKeyDataType() {
        return keyDataType;
    }

    public void setKeyDataType(DaxDataType keyDataType) {
        this.keyDataType = keyDataType;
    }

    public DaxDataType getValueDataType() {
        return valueDataType;
    }

    public void setValueDataType(DaxDataType valueDataType) {
        this.valueDataType = valueDataType;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public DaxTag getEntityTag() {
        return entityTag;
    }

    public void setEntityTag(DaxTag entityTag) {
        this.entityTag = entityTag;
    }

}

