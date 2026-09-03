package org.daxprotocol.core.annotation;

import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.reflect.Type;

public class DaxAnnotationNote {


    DaxTag entityTag;
    DaxTag tag;
    DaxTag referenceTypeTag;
    DaxDataType daxDataType;
    String name = "";
    String description = "";
    Class<?> clazz = Void.class;
    Type genericType;
    DaxDataType keyDataType;
    DaxDataType valueDataType;
    boolean readOnly = false;




    String schemaOwner = "";


    ///***************************************************
    /// Annotation  Deprecated or DaxpDeprecated
    ///
    boolean isDeprecated = false;
    String  since; //This same as original Deprecated
    boolean forRemoval = false; //This same as original  Deprecated
    String  removalVersion;
    String  replacement;
    String  reason;


    public boolean isDeprecated() {
        return isDeprecated;
    }

    public void setDeprecated(boolean deprecated) {
        isDeprecated = deprecated;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public boolean isForRemoval() {
        return forRemoval;
    }

    public void setForRemoval(boolean forRemoval) {
        this.forRemoval = forRemoval;
    }

    public String getRemovalVersion() {
        return removalVersion;
    }

    public void setRemovalVersion(String removalVersion) {
        this.removalVersion = removalVersion;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

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
    public Type getGenericType() {
        return genericType;
    }

    public void setGenericType(Type genericType) {
        this.genericType = genericType;
    }

    public String getSchemaOwner() {
        return schemaOwner;
    }

    public void setSchemaOwner(String schemaOwner) {
        this.schemaOwner = schemaOwner;
    }

    public DaxTag getReferenceTypeTag() {
        return referenceTypeTag;
    }

    public void setReferenceTypeTag(DaxTag referenceTypeTag) {
        this.referenceTypeTag = referenceTypeTag;
    }

}

