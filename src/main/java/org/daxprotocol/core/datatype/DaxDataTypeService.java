package org.daxprotocol.core.datatype;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class DaxDataTypeService {
    private static final Logger logger = LoggerFactory.getLogger(DaxDataTypeService.class);

    //--------------------------------------------------------------------------------------
    public DaxDataTypeService() {
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
        if (Map.class.isAssignableFrom(clazz)) {
            return true;
        }

        if (List.class.isAssignableFrom(clazz)) {
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
    public DaxDataType decodeFromObject(Object obj) {
//        return dataTypeCollectionService.decodeClass(obj.getClass());
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
      //  if (obj instanceof Enum<?>) return DaxDataType.STRING;   //?????????

        if (isObjInstanceOfCollection( obj )) return DaxDataType.COLLECTION;

        //Add entity ???

        return DaxDataType.UNKNOWN;

    }
    //--------------------------------------------------------------------------------------
    public    DaxDataType decodeClass(Type type) {
        return decodeClass(castReflectTypeToClass(type));
    }
    //--------------------------------------------------------------------------------------
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
    public  boolean isPrimitiveType(Class<?> clazz){
        if (clazz == String.class) return true;
        if (clazz.equals(Integer.class)) return true;
        if (clazz.equals(int.class)) return true ;
        if (clazz.equals(char.class)) return true;
        if (clazz.equals(Long.class)) return true;
        if (clazz.equals(BigDecimal.class)) return true;
        if (clazz.equals(Double.class))  return true;
        if (clazz.equals(Boolean.class)) return true;
        if (clazz.equals(LocalDate.class)) return true;
        if (clazz.equals(LocalDateTime.class)) return true;
        if (clazz.equals(Character.class)) return true;

        return false;
    }

    //--------------------------------------------------------------------------------------
    public  Class<?> castReflectTypeToClass(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }
        return null;
    }
    //--------------------------------------------------------------------------------------
     public DaxCollectionInfo getCollectionInfo(Class<?> clazz){
         DaxCollectionInfo info = new DaxCollectionInfo();

//         if (clazz == Set.class){  // is collection
//             info.isCollection = true;
//         }

         if (clazz == List.class){
             info.isColAllowDuplicates = true;
//             info.isCollection = true;
         }

         if (clazz.isEnum() || clazz.equals(Enum.class)
         ){
             info.isCollection = true;
             info.isColHasKey = true;
             info.isColDictionary = true;
             info.isJavaEnum = true;
         }

         if (clazz == Map.class
             || Map.class.isAssignableFrom(clazz)
             || clazz == HashMap.class
         ){
             info.isCollection = true;
             info.isColHasKey = true;
         }

         if (clazz == Queue.class){
             info.isCollection = true;
         }
         if (clazz == LinkedList.class){
             info.isCollection = true;
             info.isColNavigable = true;
         }


         if (Collection.class.isAssignableFrom(clazz)) {
             info.isCollection = true;  ///??? inmutable m
         }


         if (!info.isCollection){
             throw new RuntimeException("It is NOT COLLECTION !!!");
         }

         return info;
     }

       //--------------------------------------------------------------------------------------


}
