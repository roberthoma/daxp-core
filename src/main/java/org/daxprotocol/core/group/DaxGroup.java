package org.daxprotocol.core.group;

import org.daxprotocol.core.mapper.DaxReference;
import org.daxprotocol.core.mapper.DaxStringReference;

public class DaxGroup implements DaxStringReference {
    int id;
    String name;
    String namespace = "";
    String description = "";


    public DaxGroup(int id, String name) {
        this.id = id;
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

    @Override
    public String getReference() {
        return name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override public void setId(int id) {
        this.id=id;
    }
}
