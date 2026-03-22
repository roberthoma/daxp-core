package org.daxprotocol.core.group;

import org.daxprotocol.core.mapper.DaxReference;
import org.daxprotocol.core.mapper.DaxStringReference;
import org.daxprotocol.core.model.tag.DaxTag;

//public class DaxGroup implements DaxStringReference {
//TODO change to scheme
public class DaxGroup {
    DaxTag tag;
    String name;
    String namespace = "";
    String description = "";


    public DaxGroup(DaxTag tag, String name) {
        this.tag = tag;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getDescription() {
        return description;
    }

//    @Override
//    public String getReference() {
//        return name;
//    }

//    @Override
    public DaxTag getTag() {
        return tag;
    }

//    @Override
    public void setTag(DaxTag tag) {
        this.tag = tag;
    }
}
