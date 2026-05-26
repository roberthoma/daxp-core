package org.daxprotocol.core.dictionary;

import org.daxprotocol.core.annotation.DaxAnnotationNote;
import org.daxprotocol.core.annotation.DaxpCollection;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.model.tag.DaxTagDestiny;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

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

    public void registerByNote(DaxAnnotationNote annNote,
                          DaxRegisterSource source,
                          DaxTagDestiny destiny

    ){
        logger.info("Tag {}, name:{} description : {}", tagCodec.encode(annNote.getTag())
                                                      , annNote.getName()
                                                      , annNote.getDescription());

        dictionary.putTag(annNote.getTag(), source, destiny);


        if (annNote.getDaxDataType() != DaxDataType.UNKNOWN
            && annNote.getDaxDataType() != DaxDataType.NONE
            && annNote.getDaxDataType() != null
        )

        {
            dictionary.putTagAtrDataType(annNote.getTag(),annNote.getDaxDataType());
        }

        //TODO develop uniformity checking of class tags with fields
        if (! annNote.getClazz().equals(Void.class)) {

            //---------
            //TODO check is reference datatype

            if( annNote.getClazz().isAnnotationPresent(DaxpCollection.class)){

                DaxpCollection dicAnn = annNote.getClazz().getAnnotation(DaxpCollection.class);
                DaxTag tagTT =  tagCodec.decode(dicAnn);
                dictionary.putTagAttributes(annNote.getTag()
                        , Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_DATA_TYPE, tagTT)));

            }
            else {

               dictionary.putTagAttributes(annNote.getTag()
                                      , dataTypeCodec.encode(annNote.getClazz(), annNote.getGenericType()));
            }
        }


        if (destiny.equals(DaxTagDestiny.ENTITY_FIELD) ||
            destiny.equals(DaxTagDestiny.ENTITY_VALUE)  ){
            dictionary.putEntityField( annNote.getEntityTag(),annNote.getTag());
            dictionary.putEntityEntryAtrName(annNote.getEntityTag(), annNote.getTag(), annNote.getName());
            dictionary.putEntityEntryAtrDescription(annNote.getEntityTag(),annNote.getTag(), annNote.getDescription());

            if(annNote.isReadOnly()){
                dictionary.putEntityEntryAtrReadOnly(annNote.getEntityTag(),annNote.getTag(), true);
            }

            if (annNote.isDeprecated()){
               dictionary.putEntityEntryAtrDeprecated(annNote.getEntityTag(),annNote.getTag());
            }

        }

        if (destiny.equals(DaxTagDestiny.TAG) ){
            if(annNote.isReadOnly()){
                dictionary.putTagAtrReadOnly(annNote.getTag(), true);
            }


            dictionary.putTagAtrDescription(annNote.getTag(), annNote.getDescription() );

        }

        if (destiny.equals(DaxTagDestiny.TAG) || destiny.equals(DaxTagDestiny.ENTITY)){
            if (annNote.isDeprecated()){
                dictionary.putTagAtrDeprecated(annNote.getTag());
            }
        }


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


