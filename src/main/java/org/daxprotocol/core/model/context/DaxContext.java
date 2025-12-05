package org.daxprotocol.core.model.context;

public class DaxContext {
    public int id;
    public String tagPrefix;
    public String symbol;
    public String description;
    // String serviceSymbol ....
    //URL address / IP / port for service  ...  List of alternative address

    //IDEA  it passible define context and point service, for example
    /*
       <context_symbol>.<service_symbol>:<tag>=<value>
CRM.S2:130=Jon

     */
    @Override
    public String toString() {
        return "DaxContext{id=" + id +
                ", tagPrefix='" + tagPrefix + '\'' +
                ", symbol='" + symbol + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
