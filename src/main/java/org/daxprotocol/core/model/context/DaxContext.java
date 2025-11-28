package org.daxprotocol.core.model.context;

public class DaxContext {
    public int id;
    public String tagPrefix;
    public String symbol;
    public String description;


    @Override
    public String toString() {
        return "DaxContext{id=" + id +
                ", tagPrefix='" + tagPrefix + '\'' +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
