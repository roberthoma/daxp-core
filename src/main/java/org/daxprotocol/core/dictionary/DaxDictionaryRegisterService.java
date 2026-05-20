package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.annotation.DaxAnnotationNote;
import org.daxprotocol.core.annotation.DaxpDeprecated;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.tag.DaxTagDestiny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaxDictionaryRegisterService {
    private static final Logger logger = LoggerFactory.getLogger(DaxDictionaryRegisterService.class);

    DaxDictionary dictionary;
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;

    public DaxDictionaryRegisterService(DaxDictionary dictionary,
        DaxTagCodec tagCodec,
        DaxDataTypeCodec dataTypeCodec

    ){
        this.dictionary = dictionary;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;

    }

    public void regByNote(DaxAnnotationNote annNote,
                          DaxRegisterSource source,
                          DaxTagDestiny destiny

    ){
        dictionary.putTag(annNote.getTag(), source, destiny);

        if (annNote.isReadOnly()) {
            dictionary.putTagAtrReadOnly(annNote.getTag(),Boolean.TRUE);
        }
        if (annNote.getDaxDataType() != DaxDataType.UNKNOWN
            && annNote.getDaxDataType() != DaxDataType.NONE
            && annNote.getDaxDataType() != null
        )

        {
            dictionary.putTagAtrDataType(annNote.getTag(),annNote.getDaxDataType());
        }

        if (! annNote.getClazz().equals(Void.class)) {
            dictionary.putTagAttributes(annNote.getTag(), dataTypeCodec.encode(annNote.getClazz()));
        }

        logger.info("Tag {} description : {}",tagCodec.encode(annNote.getTag()) ,annNote.getDescription());



    }
}



//------------------

//    private void  putAtrDeprecated(DaxTag tag, DaxpDeprecated daxpDeprecated){
//        dictionary.putAtrDeprecated(tag);
//    }
//
//    private void  putAtrDeprecated(DaxTag tag, Deprecated deprecated){
//        dictionary.putAtrDeprecated(tag);
//    }

/*
        if (field.isAnnotationPresent(Deprecated.class)) {
        //putAtrDeprecated(tag, field.getAnnotation(DaxpDeprecated.class));
        dictionary.putEntityEntryAtrDeprecated(entityTag,tag);
        }

                if (field.isAnnotationPresent(DaxpDeprecated .class)) {
        //putAtrDeprecated(tag, field.getAnnotation(DaxpDeprecated.class));
        dictionary.putEntityEntryAtrDeprecated(entityTag,tag);
        }
//--------
*/


