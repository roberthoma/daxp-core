package org.daxprotocol.core.register;

import org.daxprotocol.core.annotation.DaxpDictionary;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.mapper.DaxContextMapper;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPopulatorEnumType {
    DaxConfig config;
    DaxContextMapper contextMapper;
    DaxRegister daxDic;
    DaxDataTypeCodec dataTypeCodec;
   public DaxPopulatorEnumType (DaxConfig config,
                               DaxContextMapper contextMapper,
                               DaxRegister daxDic,
           DaxDataTypeCodec dataTypeCodec
    ){
        this.config        = config;
        this.contextMapper = contextMapper;
        this.daxDic        = daxDic;
        this.dataTypeCodec = dataTypeCodec;
    }


    public void populate( Class<?> clazz ){
        DaxpDictionary groupAtn =  clazz.getAnnotation(DaxpDictionary.class);
        String enumName = !groupAtn.name().isBlank() ? groupAtn.name() :
                clazz.getSimpleName();


        DaxTag enumTag = DaxTag.of(config.getAppContextId(),groupAtn.tagId());

        daxDic.putEnum(enumTag, new DaxEnum(enumName,groupAtn.description()) );

        Object[] constants = clazz.getEnumConstants();

        for (Object c : constants) {
            daxDic.putEnumValue(enumTag, new DaxEnumValue(c.toString(),""));
        }

        daxDic.putAtrDataType(enumTag,dataTypeCodec.encode(Enum.class) );
        System.out.println("test dic");
    }

}
