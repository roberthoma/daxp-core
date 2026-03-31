package org.daxprotocol.core.ut.dax_00_05_base_splitter;

import org.daxprotocol.core.parsers.DaxToolPreamble;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DaxSplitterPreamble {
    static DaxToolPreamble splitterPreamble = new DaxToolPreamble();
    @Test
    void preambleSlitter_test1(){
        String preamble = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|";
        String msgStr = preamble+
                         "9=CDD|6=4" +
                        "|7=1|5=INST|100=2000|2080=Big bike|2001=123|\n2002=Robert|2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL" +
                        "|7=2|5=INST|100=2101|  2114 =Polna 7|\n ADR:2115=Warszawa|2111=345" +
                        "|7=3|5=INST|100=2102|2114=Lipińskiego 1000|2115=Sanok|2120=4|2111=3346" +
                        "|7=4|5=INST|100=2120|2121=43-444|2122=Zakopane|99=177|"+
                        "|9=CDD|6=4" +
                        "|7=1|5=INST|100=2000|2080=Small boll|2001=123|FIX:2002=Kasia|2101=2|FIX:2085=23|2102=3|2075=INDIVIDUAL" +
                        "|7=2|5=INST|100=2101|2114=Polna 8|ADR:2115=Kraków|2111=333" +
                        "|7=3|5=INST|100=2102|2114=Sowia 1000|2115=Lesko\n testline|2120=4|2111=3346" +
                        "|7=4|5=INST|100=2120|2121=44-444|2122=Rzeszów|99=177|";
        ;

        int idx = splitterPreamble.getEndOfPreambleIndex(msgStr);

        Assertions.assertEquals(preamble , msgStr.substring(0,idx));

    }

    @Test
    void preambleSlitter_test2(){
        String preamble = "DAXP=v0.1.0|EN=UTF-8|CX=CRM|";
        String msgStr = preamble+
                "\n9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Big bike|2001=123|\n2002=Robert|2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL" +
                "|7=2|5=INST|100=2101|  2114 =Polna 7|\n ADR:2115=Warszawa|2111=345" +
                "|7=3|5=INST|100=2102|2114=Lipińskiego 1000|2115=Sanok|2120=4|2111=3346" +
                "|7=4|5=INST|100=2120|2121=43-444|2122=Zakopane|99=177|";
        ;

        int idx = splitterPreamble.getEndOfPreambleIndex(msgStr);

        Assertions.assertEquals(preamble , msgStr.substring(0,idx));

    }
    @Test
    void preambleSlitter_test3(){
        String preamble = "DAXP=v0.1.0|EN=UTF-8|CX=CRM |";
        String msgStr = preamble+
                "\n $:9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Big bike|2001=123|\n2002=Robert|2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL|";
        ;
        int idx = splitterPreamble.getEndOfPreambleIndex(msgStr);

        Assertions.assertEquals(preamble , msgStr.substring(0,idx));

    }

    @Test
    void preambleSlitter_test6(){
        String preamble = "DAXP=v0.1.0|EN=UTF-8|CX=CRM |";
        String msgStr = preamble+
                "\n $ : 9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Big bike|2001=123|\n2002=Robert|2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL|";
        ;
        int idx = splitterPreamble.getEndOfPreambleIndex(msgStr);

        Assertions.assertEquals(preamble , msgStr.substring(0,idx));

    }


    @Test
    void preambleSlitter_test7(){
        String preamble = "DAXP=v0.1.0|EN=UTF-8|CX=CRM |";
        String msgStr = preamble+
                "\n  : 9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Big bike|2001=123|\n2002=Robert|2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL|";
        ;
        int idx = splitterPreamble.getEndOfPreambleIndex(msgStr);

        Assertions.assertEquals(preamble , msgStr.substring(0,idx));

    }
    @Test
    void preambleSlitter_test8(){
        String preamble = "DAXP=v0.1.0|EN=UTF-8|CX=CRM |";
        String msgStr = preamble+
                "A:9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Big bike|2001=123|\n2002=Robert|2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL|";
        ;
        int idx = splitterPreamble.getEndOfPreambleIndex(msgStr);

        Assertions.assertEquals(-1, idx);

    }
    //TODO  $$:9 Splitter not work
    void preambleSlitter_test10(){
        String preamble = "DAXP=v0.1.0|EN=UTF-8|CX=CRM |";
        String msgStr = preamble+
                "$$:9=CDD|6=4" +
                "|7=1|5=INST|100=2000|2080=Big bike|2001=123|\n2002=Robert|2101=2|FIX:2085=345|2102=3|2075=INDIVIDUAL|";
        ;
        int idx = splitterPreamble.getEndOfPreambleIndex(msgStr);

        Assertions.assertEquals(-1, idx);

    }

}
