package org.daxprotocol.core.application;

public class DaxCoreConfig {

    private int minBulkSize;
    private int defaultMinBulkSize = 3;

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
        minBulkSize = defaultMinBulkSize;

    }

}
