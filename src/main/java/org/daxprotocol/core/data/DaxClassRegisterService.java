package org.daxprotocol.core.data;

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

public class DaxClassRegisterService {
    private static final Logger logger = LoggerFactory.getLogger(DaxClassRegisterService.class);

    DaxDataModel dataModel;
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;

    public DaxClassRegisterService(DaxDataModel dataModel,
        DaxTagCodec tagCodec,
        DaxDataTypeCodec dataTypeCodec

    ){
        this.dataModel = dataModel;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;

    }

    public void registerByNote(DaxAnnotationNote annNote,
                          DaxRegisterSource source,
                          DaxTagDestiny destiny

    ){
        logger.info("registerByNote > Tag {}, name:{} description : {}", tagCodec.encode(annNote.getTag())
                                                      , annNote.getName()
                                                      , annNote.getDescription());

        dataModel.putTag(annNote.getTag(), source, destiny);


        if (annNote.getDaxDataType() != DaxDataType.UNKNOWN
            && annNote.getDaxDataType() != DaxDataType.NONE
            && annNote.getDaxDataType() != null
        )

        {
            dataModel.putTagAtrDataType(annNote.getTag(),annNote.getDaxDataType());
        }

        //TODO develop uniformity checking of class tags with fields
        if (! annNote.getClazz().equals(Void.class)) {

            //---------
            //TODO check is reference datatype

            if( annNote.getClazz().isAnnotationPresent(DaxpCollection.class)){

                DaxpCollection dicAnn = annNote.getClazz().getAnnotation(DaxpCollection.class);
                DaxTag tagTT =  tagCodec.decode(dicAnn);
                dataModel.putTagAttributes(annNote.getTag()
                        , Set.of(new DaxPairTag(DaxCoreTags.ATR_REF_DATA_TYPE, tagTT)));

            }
            else {

               dataModel.putTagAttributes(annNote.getTag()
                                      , dataTypeCodec.encode(annNote.getClazz(), annNote.getGenericType()));
            }
        }


        if (destiny.equals(DaxTagDestiny.ENTITY_FIELD) ||
            destiny.equals(DaxTagDestiny.ENTITY_VALUE)  ){
            dataModel.putEntityField( annNote.getEntityTag(),annNote.getTag());
            dataModel.putEntityEntryAtrName(annNote.getEntityTag(), annNote.getTag(), annNote.getName());
            dataModel.putEntityEntryAtrDescription(annNote.getEntityTag(),annNote.getTag(), annNote.getDescription());

            if(annNote.isReadOnly()){
                dataModel.putEntityEntryAtrReadOnly(annNote.getEntityTag(),annNote.getTag(), true);
            }

            if (annNote.isDeprecated()){
               dataModel.putEntityEntryAtrDeprecated(annNote.getEntityTag(),annNote.getTag());
            }

        }

        if (destiny.equals(DaxTagDestiny.TAG) ){
            if(annNote.isReadOnly()){
                dataModel.putTagAtrReadOnly(annNote.getTag(), true);
            }


            dataModel.putTagAtrDescription(annNote.getTag(), annNote.getDescription() );

        }

        if (destiny.equals(DaxTagDestiny.TAG) || destiny.equals(DaxTagDestiny.ENTITY)){
            if (annNote.isDeprecated()){
                dataModel.putTagAtrDeprecated(annNote.getTag());
            }
        }


    }
}


