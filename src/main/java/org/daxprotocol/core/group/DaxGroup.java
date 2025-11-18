package org.daxprotocol.core.group;

public class DaxGroup implements DaxpGroupItf{

    public DaxGroup(int id, int masterId, String name) {
        this.id = id;
        this.masterId = masterId;
        this.name = name;
    }

    int id;
    int masterId;
    String name;
    String namespace = "";
    String description = "";


    @Override public int getId() {
        return id;
    }


    @Override public int getMasterId() {
        return masterId;
    }

    @Override public String getName() {
        return name;
    }

    @Override public String getNamespace() {
        return namespace;
    }

    @Override public String getDescription() {
        return description;
    }
}
