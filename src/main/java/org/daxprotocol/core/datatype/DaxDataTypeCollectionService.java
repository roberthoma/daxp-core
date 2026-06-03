package org.daxprotocol.core.datatype;

import org.daxprotocol.core.annotation.DaxpCollection;
import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairBoolean;
import org.daxprotocol.core.model.pair.DaxPairDataType;
import org.daxprotocol.core.model.pair.DaxPairTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.daxprotocol.core.application.DaxCoreTags.*;
import static org.daxprotocol.core.application.DaxCoreTags.ATR_DATA_TYPE;
import static org.daxprotocol.core.application.DaxCoreTags.COLLECTION_IS_DICTIONARY;
import static org.daxprotocol.core.application.DaxCoreTags.COLLECTION_NAVIGABLE;
import static org.daxprotocol.core.application.DaxCoreTags.COLLECTION_KEY_DATA_TYPE;
import static org.daxprotocol.core.application.DaxCoreTags.COLLECTION_VALUE_DATA_TYPE;

public class DaxDataTypeCollectionService {
    private static final Logger logger = LoggerFactory.getLogger(DaxDataTypeCollectionService.class);
    DaxTagCodec tagCodec;

    public DaxDataTypeCollectionService(DaxTagCodec tagCodec) {
        this.tagCodec = tagCodec;
    }
    //--------------------------------------------------------------------------------------
    public boolean isCollection(Class<?> clazz) {


        if (clazz.equals(List.class)) return true;
        if (clazz.equals(Map.class)) return true;
        if (clazz.equals(Set.class)) return true;
        if (clazz.equals(Queue.class)) return true;
        if (clazz.equals(Collection.class)) return true;
        if (clazz.equals(Enum.class)) return true;    //TODO CHECK again
        if (clazz.isEnum()) return true;

        if (Collection.class.isAssignableFrom(clazz)) {
            return true;
        }


        return false;
    }

    //--------------------------------------------------------------------------------------
    public boolean isObjInstanceOfCollection(Object obj){
        if (obj instanceof List<?>) return true;
        if (obj instanceof Map<?,?>) return true;
        if (obj instanceof Set<?>) return true;
        if (obj instanceof Collection<?>) return true;
  //      if (obj instanceof Enum<?>) return true;
        return false;
    }
    //--------------------------------------------------------------------------------------
  /*
    public DaxDataType decodeFromObject(Object obj) {
        return dataTypeCollectionService.decodeClass(obj.getClass());
        /*
        if (obj == null) return DaxDataType.STRING; // Default
        if (obj instanceof Integer) return DaxDataType.INTEGER;
        if (obj instanceof Long) return DaxDataType.LONG;
        if (obj instanceof BigDecimal) return DaxDataType.DECIMAL;
        if (obj instanceof Double) return DaxDataType.DOUBLE;
        if (obj instanceof Boolean) return DaxDataType.BOOLEAN;
        if (obj instanceof LocalDate) return DaxDataType.LOCAL_DATE;
        if (obj instanceof LocalDateTime) return DaxDataType.LOCAL_DATE_TIME;
        if (obj instanceof String) return DaxDataType.STRING;
        if (obj instanceof Character) return DaxDataType.CHARACTER;
        if (obj instanceof Enum<?>) return DaxDataType.STRING;   //?????????

        if (dataTypeCollectionService.isObjInstanceOfCollection( obj )) return DaxDataType.COLLECTION;

        //Add entity ???

        return DaxDataType.UNKNOWN;

    }
*/
    public    DaxDataType decodeClass(Class<?> clazz) {
        if (clazz == null) { return DaxDataType.UNKNOWN;}

        if (clazz == String.class) return DaxDataType.STRING;
        if (clazz.equals(Integer.class)) return DaxDataType.INTEGER;
        if (clazz.equals(int.class)) return DaxDataType.INTEGER;
        if (clazz.equals(char.class)) return DaxDataType.CHARACTER;
        if (clazz.equals(Long.class)) return DaxDataType.LONG;
        if (clazz.equals(BigDecimal.class)) return DaxDataType.DECIMAL;
        if (clazz.equals(Double.class)) return DaxDataType.DOUBLE;
        if (clazz.equals(Boolean.class)) return DaxDataType.BOOLEAN;
        if (clazz.equals(LocalDate.class)) return DaxDataType.LOCAL_DATE;
        if (clazz.equals(LocalDateTime.class)) return DaxDataType.LOCAL_DATE_TIME;
        if (clazz.equals(Character.class)) return DaxDataType.CHARACTER;
        if (clazz.isEnum()) return DaxDataType.STRING;

        if(isCollection(clazz)) return DaxDataType.COLLECTION;

        if (clazz.isAnnotationPresent(DaxpEntity.class)) return DaxDataType.ENTITY;


        return DaxDataType.UNKNOWN;
    }
    //--------------------------------------------------------------------------------------
    public  Class<?> getClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        return null;
    }
    //--------------------------------------------------------------------------------------
    public Set<DaxPair<?>> collectionEncode(Class<?> clazz, Type generitType){
        Set< DaxPair<?>> map = new HashSet<>();
        boolean isCollection = false;
        boolean isColAllowDuplicates = false;
        boolean isColHasKey = false;
        boolean isColDictionary = false;
        boolean isColNavigable = false;
        boolean isJavaEnum = false;




        if (clazz == Set.class){
            isCollection = true;
        }

        if (clazz == List.class){
            isColAllowDuplicates = true;
            isCollection = true;
        }

        if (clazz.isEnum() || clazz.equals(Enum.class)
        ){
            isCollection = true;
            isColHasKey = true;
            isColDictionary = true;
            isJavaEnum = true;
        }

        if (clazz == Map.class){
            isCollection = true;
            isColHasKey = true;
        }

        if (clazz == Queue.class){
            isCollection = true;
        }
        if (clazz == LinkedList.class){
            isCollection = true;
            isColNavigable = true;
        }


        if (Collection.class.isAssignableFrom(clazz)) {
            isCollection = true;  ///??? inmutable m
        }


        if (!isCollection){
            throw new RuntimeException("It is NOT COLLECTION !!!");
        }

                                  map.add(new DaxPairDataType(ATR_DATA_TYPE,DaxDataType.COLLECTION));

        if(isColAllowDuplicates)  map.add(new DaxPairBoolean(COLLECTION_ALLOW_DUPLICATES,true));
        if(isColHasKey)           map.add(new DaxPairBoolean(COLLECTION_HAS_KEY,true));
        if(isColDictionary)       map.add(new DaxPairBoolean(COLLECTION_IS_DICTIONARY,true));
        if(isColNavigable)        map.add(new DaxPairBoolean(COLLECTION_NAVIGABLE,true));


       //**************************************

        DaxDataType valueDataType = DaxDataType.NONE;
        DaxDataType keyDataType = DaxDataType.NONE;;

        if (generitType instanceof ParameterizedType pt) {

            logger.info( "GeneritType.getTypeName()= {}", generitType.getTypeName());

            Type rawType = pt.getRawType();
            Type[] args = pt.getActualTypeArguments();
//            Annotation ann;
            if (isColHasKey){
                keyDataType   = decodeClass( getClass(args[0]));
                valueDataType = decodeClass( getClass(args[1]));

                map.add(new DaxPairDataType(COLLECTION_KEY_DATA_TYPE,keyDataType));
                map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,valueDataType));

                if(valueDataType.equals(DaxDataType.ENTITY))
                {
                    DaxpEntity entAnn =  getClass(args[1]).getAnnotation(DaxpEntity.class);
                    map.add(new DaxPairTag(COLLECTION_VALUE_TYPE_ID,tagCodec.decode(entAnn)));
                }


                if(valueDataType.equals(DaxDataType.COLLECTION))
                {
                    if (getClass(args[1]).isAnnotationPresent(DaxpCollection.class)){
                        DaxpCollection colAnn =  getClass(args[1]).getAnnotation(DaxpCollection.class);
                        map.add(new DaxPairTag(COLLECTION_VALUE_TYPE_ID,tagCodec.decode(colAnn)));
                    }
                }


            }else {
                valueDataType = decodeClass( getClass(args[0]));
                map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,valueDataType));
                if(valueDataType.equals(DaxDataType.ENTITY))
                {
                    DaxpEntity entAnn =  getClass(args[0]).getAnnotation(DaxpEntity.class);
                    map.add(new DaxPairTag(COLLECTION_VALUE_TYPE_ID,tagCodec.decode(entAnn)));
                }

            }
        }

        if(isJavaEnum){
            map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,DaxDataType.STRING));
        }

        return map;

    }
    //--------------------------------------------------------------------------------------


}
