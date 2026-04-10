package org.daxprotocol.core.unit_test.dax_00_20_base_parser;

import org.junit.jupiter.api.Test;

public class DaxTMPTest {


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


     //   Assertions.assertEquals(preamble , msgStr.substring(0,idx));

    }


}
