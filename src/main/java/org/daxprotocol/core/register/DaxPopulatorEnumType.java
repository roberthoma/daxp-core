package org.daxprotocol.core.register;

import org.daxprotocol.core.annotation.DaxpEnum;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxEnum;
import org.daxprotocol.core.dictionary.DaxEnumValue;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.tag.DaxTag;

public class DaxPopulatorEnumType {
    DaxConfig config;
    DaxStringReferenceMapper contextMapper;
    DaxDictionary daxDic;
   public DaxPopulatorEnumType (DaxConfig config,
                               DaxStringReferenceMapper contextMapper,
                               DaxDictionary daxDic
    ){
        this.config        = config;
        this.contextMapper = contextMapper;
        this.daxDic        = daxDic;
    }


    public void populate( Class<?> clazz ){
        DaxpEnum groupAtn =  clazz.getAnnotation(DaxpEnum.class);
        String enumName = !groupAtn.name().isBlank() ? groupAtn.name() :
                clazz.getSimpleName();


        DaxTag enumTag = DaxTag.of(config.getAppContextId(),groupAtn.tagId());

        daxDic.putEnum(enumTag, new DaxEnum(enumName,groupAtn.description()) );

        Object[] constants = clazz.getEnumConstants();

        for (Object c : constants) {
            daxDic.putEnumValue(enumTag, new DaxEnumValue(c.toString(),""));
        }
        daxDic.putAtrDataType(enumTag, Enum.class );

    }

}
