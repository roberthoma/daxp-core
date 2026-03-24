package org.daxprotocol.core.dictionary.populator;

import org.daxprotocol.core.annotation.DaxpDTO;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.dictionary.DaxDictionary;
import org.daxprotocol.core.dictionary.DaxEnum;
import org.daxprotocol.core.dictionary.DaxEnumValue;
import org.daxprotocol.core.mapper.DaxStringReferenceMapper;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.rules.DaxParserService;

public class DaxPopulatorEnumType {
    DaxpConfig config;
    DaxStringReferenceMapper contextMapper;
    DaxParserService parserService;

   public DaxPopulatorEnumType (DaxpConfig config,
    DaxStringReferenceMapper contextMapper,
    DaxParserService parserService
    ){
        this.config        = config;
        this.contextMapper = contextMapper;
        this.parserService = parserService;
    }


    public void populate(DaxDictionary daxDic, Class<?> clazz ){
        DaxpDTO groupAtn =  clazz.getAnnotation(DaxpDTO.class);


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
