package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.annotation.DaxpCollection;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPopulatorEnumType {
    DaxConfig config;

    DaxDictionary daxDic;
    DaxDataTypeCodec dataTypeCodec;
   public DaxPopulatorEnumType (DaxConfig config,
                               DaxDictionary daxDic,
           DaxDataTypeCodec dataTypeCodec
    ){
        this.config        = config;
        this.daxDic        = daxDic;
        this.dataTypeCodec = dataTypeCodec;
    }


    public void populate( Class<?> clazz ){
        DaxpCollection groupAtn =  clazz.getAnnotation(DaxpCollection.class);
        String enumName = !groupAtn.name().isBlank() ? groupAtn.name() :
                clazz.getSimpleName();


        DaxTag enumTag = DaxTag.of(config.getAppContextId(),groupAtn.tagId());

        daxDic.putEnum(enumTag, new DaxCollection_TMP(enumName,groupAtn.description()) );

        Object[] constants = clazz.getEnumConstants();

        for (Object c : constants) {
            daxDic.putEnumValue(enumTag, new DaxEnumValue(c.toString(),""));
        }

        daxDic.putTagAttributes(enumTag,dataTypeCodec.encode(Enum.class) );
        System.out.println("test dic");
    }

}
