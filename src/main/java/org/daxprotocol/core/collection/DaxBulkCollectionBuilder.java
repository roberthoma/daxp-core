package org.daxprotocol.core.collection;

import org.daxprotocol.core.application.DaxCoreConstants;

public class DaxBulkCollectionBuilder {

    public String build(){
        StringBuffer sb = new StringBuffer();

        //TMP SOLUTION
        sb.append(DaxCoreConstants.SEPARATOR_FILE)
                .append("1001").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("1002").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("1003").append(DaxCoreConstants.SEPARATOR_UNIT)
                .append("1004")
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
                ;




//      SEPARATOR_FILE   = 0x001D; //<FS>	File Separator
//      SEPARATOR_GROUP  = 0x001D; //<GR> Group Separator
//      SEPARATOR_RECORD = 0x001E; //<RS> Record Separator
//      SEPARATOR_UNIT   = 0x001F; //<US> Unit Separator

/*


        $:230=<FS>$:201=1001;1002;1003<FS>203=1011;1012<RS>
              <GS>  1  <US> 35 <US> 55  <GS>  ABC  <US> xvd  <RS>
              <GS>  24 <US> 6  <US> 23  <GS>  DES  <US> sdef <RS>
              <GS>  3  <US> 5  <US> 5   <GS>  DWW  <US> fgdfg <RS>
             |---------------KEY- --------|------VALUE --------|
*/
        return sb.toString();
//        return sb.toString().replace(DaxCoreConstants.SEPARATOR_UNIT,'#').replace(DaxCoreConstants.SEPARATOR_RECORD,'%');
    }

}
