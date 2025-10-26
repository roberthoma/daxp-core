package org.daxprotocol.core.attributes;

public abstract class DaxAttribute <T>{

    int tag;
    T value;
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

    public DaxAttribute (int tag, T value){
        this.tag = tag;
        this.value = value;

    }

}
