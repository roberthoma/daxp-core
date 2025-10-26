package org.daxprotocol.core.model.body;

import java.util.HashMap;
import java.util.Map;

public class DaxBody {

    Map<Integer,Map<Integer, String>> blocksMap = new HashMap<>();

    int blocks = 0;

    public int getBlocks() {
        return blocks;
    }

    public Map<Integer, String> getBlock(int blockIdx){
        return blocksMap.get(blockIdx);
    }

    public DaxBody(){
//        blocksMap.put(0,new HashMap<>());  //Put first block BLOCK_INDEX = 0
    }

    public void putPair(int tag, String value){
        blocksMap.get(blocks).put(tag,value);
    }

    public void nextBlock(){
        blocks = blocksMap.size();
        blocksMap.put(blocks,new HashMap<>());
    }



}
