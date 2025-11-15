package Dax_00_Base_test;

public enum CustomerType {

    INDIVIDUAL("I","I","Natural Person"),
    ORGANIZATION("O","O" ,"Legal Entity");

    public final String id;
    public final String symbol;
    public final String desc;

    CustomerType(String id, String symbol, String desc){
        this.id = id;
        this.symbol = symbol;
        this.desc = desc;
    }


    public static CustomerType of(String id){
        for (CustomerType e : values()){
            if (e.id.equals(id)){
                return e;
            }
        }
        return null;
    }



 }
