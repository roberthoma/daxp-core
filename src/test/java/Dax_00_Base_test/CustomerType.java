package Dax_00_Base_test;

public enum CustomerType {

    INDIVIDUAL("I","Natural Person"),
    ORGANIZATION("O", "Legal Entity");

    public final String symbol;
    public final String desc;

    CustomerType(String symbol, String desc){
        this.symbol = symbol;
        this.desc = desc;


    }
 }
