package org.daxprotocol.core.codec;

public abstract class DaxPair<T>{

    int tag;
    protected T value;
    Class<T> clazz;

    public Class<?> getClazz(){
        return clazz;
    };
    public int getTag(){
        return tag;
    }
    public T getValue(){
        return value;
    }

    public DaxPair(int tag, T value){
        this.tag = tag;
        this.value = value;

    }

    public String getStrValue() {
         return value.toString();
    };
}
