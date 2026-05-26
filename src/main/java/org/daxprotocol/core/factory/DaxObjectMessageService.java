package org.daxprotocol.core.factory;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.annotation.DaxpValue;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.datatype.DaxBlockType;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.body.DaxBody;
import org.daxprotocol.core.model.pair.DaxPairString;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.tool.DaxLangTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static org.daxprotocol.core.application.DaxCoreTags.ENTRY_TAG;

public class DaxObjectMessageService {
    private static final Logger logger = LoggerFactory.getLogger(DaxObjectMessageService.class);
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    public DaxObjectMessageService(    DaxTagCodec tagCodec,DaxDataTypeCodec dataTypeCodec){
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
    }


    public void objectToMsgBlock(int blockIdx,
                                DaxTag blockTag,
                                Object entry,
                                DaxBody body,
                                Set<DaxTag> reqTagSet
                               )
    {
        body.putPair(blockIdx, ENTRY_TAG, blockTag);
        try {
            for (Field field : DaxLangTool.allFields(entry.getClass())) {
                //----------------------------------------
                if (field.isAnnotationPresent(DaxpField.class)) {
                    DaxpField fieldAnn = field.getAnnotation(DaxpField.class);
                    field.setAccessible(true);

                    DaxTag tag = tagCodec.decode( fieldAnn);

                    if(reqTagSet != null && !reqTagSet.contains(tag)){
                        continue;
                    }
                    //TODO add refenrens to other oblck using prefix like @ or #....
                    if (   field.get(entry) != null
                            && field.get(entry).getClass().isAnnotationPresent(DaxpEntity.class))
                    {
                        body.nextBlock(DaxBlockType.BLOCK_VALUE);
                        int nestedIdx = body.getCurrentIdx();

                        body.putTagBlockReference(blockIdx, tag, (nestedIdx+1));

                        objectToMsgBlock(nestedIdx, tag,  field.get(entry),  body , reqTagSet);
                    }
                    else {
                        Object obj = field.get(entry);
                        if (obj == null){
                            body.putNullTag(blockIdx,tag);
                        }
                        else {
                           body.putPair(blockIdx, dataTypeCodec.convertToValue(tag,obj ));
                        }
                    }
                }
                if (field.isAnnotationPresent(DaxpValue.class)) {
                    DaxpValue valueAnn = field.getAnnotation(DaxpValue.class);
                    field.setAccessible(true);

                    if (field.get(entry) != null) {
                        DaxTag tag = tagCodec.decode( valueAnn);

                        if(reqTagSet != null && !reqTagSet.contains(tag)){
                            continue;
                        }
                        body.putPair(blockIdx,dataTypeCodec.convertToValue(tag, field.get(entry) ));

                    }
                }
                //----------------------------------------

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String methodName = "?";
        try {
            for (Method method : entry.getClass().getDeclaredMethods()) {
                DaxpValue methodAnn = method.getAnnotation(DaxpValue.class);
                if (methodAnn == null) continue;

                Class<?> returnType = method.getReturnType();

                methodName = method.getName();
                DaxTag tag = tagCodec.decode( methodAnn);

                if(reqTagSet != null && !reqTagSet.contains(tag)){
                    continue;
                }
                Object o = method.invoke(entry);

                body.putPair(blockIdx,new DaxPairString(tag,o.toString()));
            }
        }
        catch (IllegalAccessException e){
            logger.error("Method {} is not Public ",methodName );
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

}
