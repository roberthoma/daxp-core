package org.daxprotocol.core.collection;

import org.daxprotocol.core.application.DaxCoreConstants;

public class DaxBulkCollectionBuilder {

    public String build(){
        StringBuffer sb = new StringBuffer();

        //TMP SOLUTION
        sb.append(DaxCoreConstants.SEPARATOR_START_OF_TEXT)
                .append("tag1").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("tag2").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("tag3").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("tag4")
                .append(DaxCoreConstants.SEPARATOR_RECORD)
                .append("rec1 val1").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("rec1 val2").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("rec1 val3").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("rec1 val4")
                .append(DaxCoreConstants.SEPARATOR_RECORD)
                .append("rec2 val1").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("rec2 val2").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("rec2 val3").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("rec2 val4")
                .append(DaxCoreConstants.SEPARATOR_END_OF_TEXT);



        return sb.toString();
//        return sb.toString().replace(DaxCoreConstants.SEPARATOR_UNIT,'#').replace(DaxCoreConstants.SEPARATOR_RECORD,'%');
    }

}
