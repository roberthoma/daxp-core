package org.daxprotocol.core.model.body;

import java.util.HashMap;
import java.util.Map;

public class DaxBody {

    Map<Integer,Map<Integer, String>> blocksMap = new HashMap<>();

    int blockIdx = 0;

    public int getBlockCount() {
        return blockIdx + 1;
    }

    public Map<Integer, String> getBlock(int blockIdx){
        return blocksMap.get(blockIdx);
    }

    public void putPair(int tag, String value){
        blocksMap.get(blockIdx).put(tag,value);
    }

    public void nextBlock(){
        blockIdx = blocksMap.size();
        blocksMap.put(blockIdx,new HashMap<>());
    }



}
