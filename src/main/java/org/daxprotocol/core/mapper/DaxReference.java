package org.daxprotocol.core.mapper;

public interface DaxReference<T> {
    T getReference();
    int    getId();
    void   setId(int id );
}
