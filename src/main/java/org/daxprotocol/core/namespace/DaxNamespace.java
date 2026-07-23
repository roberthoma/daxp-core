package org.daxprotocol.core.namespace;

import org.daxprotocol.core.mapper.DaxReference;

public class DaxNamespace implements DaxReference<String> {
    private int id;
    private String tagPrefix;
    private String symbol;
    private String description;

    @Override
    public String getReference() {
        return tagPrefix;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getTagPrefix() {
        return tagPrefix;
    }

    public void setTagPrefix(String tagPrefix) {
        this.tagPrefix = tagPrefix;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
// String serviceSymbol ....
    //URL address / IP / port for service  ...  List of alternative address

    //IDEA  it passible define namespace and point service, for example
    /*
       <namespace_symbol>.<service_symbol>:<tag>=<value>
CRM.S2:130=Jon

     */
    @Override
    public String toString() {
        return "Daxnamespace{id=" + id +
                ", tagPrefix='" + tagPrefix + '\'' +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
