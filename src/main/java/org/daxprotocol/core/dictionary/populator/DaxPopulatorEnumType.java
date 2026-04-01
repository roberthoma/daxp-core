package org.daxprotocol.core.dictionary.populator;

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

   public DaxPopulatorEnumType (DaxConfig config,
                               DaxStringReferenceMapper contextMapper
    ){
        this.config        = config;
        this.contextMapper = contextMapper;
    }


    public void populate(DaxDictionary daxDic, Class<?> clazz ){
        DaxpEnum groupAtn =  clazz.getAnnotation(DaxpEnum.class);


        //        DaxDictionaryDecoratorService.printDaxGroupInfo(group);

        String enumName = !groupAtn.name().isBlank() ? groupAtn.name() :
                clazz.getSimpleName();


        DaxTag enumTag = new DaxTag(config.getAppContextId(),groupAtn.tagId());

        daxDic.putEnum(enumTag, new DaxEnum(enumName,groupAtn.description()) );
//        daxDic.getEnumDictionary(). putEnum2(enumTag, DaxLangTool.asEnumClass(clazz)  );

        Object[] constants = clazz.getEnumConstants();

        for (Object c : constants) {
            daxDic.putEnumValue(enumTag, new DaxEnumValue(c.toString(),""));
        }
        daxDic.putAtrDataType(enumTag, Enum.class );


        System.out.println("Test 123");

//                groupId = groupMapper.getReferenceId(groupAtn.name());

    }

}
