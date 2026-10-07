package org.daxprotocol.core.application;

public class DaxCoreConfig {

    private int minBulkSize;

    public DaxCoreConfig(){
        resetMinBulkSize();

    }

    public int getMinBulkSize() {
        return minBulkSize;
    }


    public void setMinBulkSize(int minBulkSize) {
        this.minBulkSize = minBulkSize;
    }

    public void resetMinBulkSize(){
        minBulkSize = 3;

    }

}
