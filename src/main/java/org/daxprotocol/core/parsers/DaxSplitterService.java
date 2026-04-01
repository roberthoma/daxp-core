package org.daxprotocol.core.parsers;

import java.util.ArrayList;
import java.util.List;

public class DaxSplitterService {

    public  List<Integer> getPipeIndices(String str) {
        if (str == null || str.isEmpty()) {
            return null; //THROW Exception
        }

        List<Integer> indexList = new ArrayList<>();

        // Loop through the string and find every occurrence
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '|') {
                indexList.add(i);
            }
        }
        return indexList;
    }



}
