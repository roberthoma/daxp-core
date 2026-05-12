package org.daxprotocol.core.datatype;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.model.pair.DaxPair;
import org.daxprotocol.core.model.pair.DaxPairBoolean;
import org.daxprotocol.core.model.pair.DaxPairDataType;

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

public class DaxDataTypeService {


    public boolean isCollection(Class<?> clazz) {

        if (clazz.equals(List.class)) return true;
        if (clazz.equals(Map.class)) return true;
        if (clazz.equals(Set.class)) return true;
        if (clazz.equals(Collection.class)) return true;
        if (clazz.equals(Enum.class)) return true;
        if (clazz.isEnum()) return true;

        return false;
    }


    public boolean isObjInstanceOfCollection(Object obj){
        if (obj instanceof List<?>) return true;
        if (obj instanceof Map<?,?>) return true;
        if (obj instanceof Set<?>) return true;
        if (obj instanceof Collection<?>) return true;
        if (obj instanceof Enum<?>) return true;
        return false;
    }

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

        if(isCollection(clazz)) return DaxDataType.COLLECTION;

        if (clazz.isAnnotationPresent(DaxpEntity.class)) return DaxDataType.ENTITY;


        return DaxDataType.UNKNOWN;
    }

    public  Class<?> getClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        return null;
    }

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

        if (!isCollection){
            throw new RuntimeException("It is NOT COLLECTION !!!");
        }


        map.add(new DaxPairDataType(ATR_DATA_TYPE,DaxDataType.COLLECTION));
        if(isColAllowDuplicates)  map.add(new DaxPairBoolean(COLLECTION_ALLOW_DUPLICATES,true));
        if(isColHasKey)           map.add(new DaxPairBoolean(COLLECTION_HAS_KEY,true));
        if(isColDictionary)       map.add(new DaxPairBoolean(COLLECTION_IS_DICTIONARY,true));
        if(isColNavigable)        map.add(new DaxPairBoolean(COLLECTION_NAVIGABLE,true));


       //****************************************8888

        DaxDataType valueDataType = DaxDataType.UNKNOWN;
        DaxDataType keyDataType = DaxDataType.UNKNOWN;;

        if (generitType instanceof ParameterizedType pt) {
            Type rawType = pt.getRawType();
            Type[] args = pt.getActualTypeArguments();

            if (isColHasKey){
                keyDataType   = decodeClass( getClass(args[0]));
                valueDataType = decodeClass( getClass(args[1]));

                map.add(new DaxPairDataType(COLLECTION_KEY_DATA_TYPE,decodeClass( getClass(args[0]))));
                map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,decodeClass( getClass(args[1]))));
            }else {
                valueDataType = decodeClass( getClass(args[0]));
                map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,decodeClass( getClass(args[0]))));
            }


            System.out.println( "**************************************************");
            System.out.println( "**");
            System.out.println( "generitType.getTypeName()="+generitType.getTypeName());
            System.out.println( "**");
            System.out.println( "**************************************************");
            //       }
        }


        if(isJavaEnum){


            map.add(new DaxPairDataType(COLLECTION_VALUE_DATA_TYPE,DaxDataType.STRING));

            //        if (clazz.)
///  Retrun as string val1, Val2,....
        // If class has implemented DaxpDictionary then value ha each block

//            Object[] constants = clazz.getEnumConstants();
//
//            if (constants != null){
//                    for (Object c : constants) {
//
//                        System.out.println(">>>>> ENUM VAL="+c.toString());
//                    }
//            }
//
//        daxDic.putTagAttributes(enumTag,dataTypeCodec.encode(Enum.class) );


        }






        return map;

    }



}
