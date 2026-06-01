/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 DAXPARC Robert Homa
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ***********************************************************************
 */

package org.daxprotocol.core.dictionary;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.application.DaxCoreTags;
import org.daxprotocol.core.codec.DaxTagCodec;
import org.daxprotocol.core.config.DaxConfig;
import org.daxprotocol.core.datatype.DaxDataTypeCodec;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class DaxMessageConverter {

    DaxConfig config;
    DaxDictionary dictionary;
    DaxTagCodec tagCodec;
    DaxDataTypeCodec dataTypeCodec;
    public DaxMessageConverter(DaxConfig config,
            DaxDictionary dictionary,
            DaxTagCodec tagCodec,
            DaxDataTypeCodec dataTypeCodec
            ) {
        this.config = config;
        this.dictionary = dictionary;
        this.tagCodec = tagCodec;
        this.dataTypeCodec = dataTypeCodec;
    }

///idea : create map <tag, field> and next by messa f
///
     Map<DaxTag, Field> fieldMap = new HashMap<>();







    public <T> T createFromMessage(DaxMessage message, Class<T> targetClass, int blockIdx ) {
        try {
            // TODO Check tha exist default contractor
            T instance = targetClass.getDeclaredConstructor().newInstance();


            for (Field field : targetClass.getDeclaredFields()) {
                DaxpField ann = field.getAnnotation(DaxpField.class);

                if (ann == null) continue; // skip non-annotated fields (e.g., town)


                DaxTag tag = tagCodec.decode(ann);
                field.setAccessible(true);

                if ( message.isNullAt(blockIdx, tag)){

                    field.set(instance, null);
                    continue;
                }

                var pair = message.get(blockIdx,tag);


                if (pair!=null) {
                    String raw = pair.getStrValue();
                    Object converted = dataTypeCodec.convert(raw, field.getType());  // if not ..convert from dictionary
                    field.set(instance, converted);
                    continue; // gracefully ignore missing tags or empty
                }

                if ( message.isAnyReference(blockIdx,tag)){
                    Set<Integer> refBlocksIdx =  message.getRefBlocksIdx(blockIdx,tag);

                    if (Collection.class.isAssignableFrom(field.getType())) {

                        Collection<Object> collection = createCollectionInstance(field.getType());

                            System.out.println("JEST LISTA >>>>");

                        for (Integer refIdx : refBlocksIdx) {
                            int targetBlockIdx = refIdx - 1;
                            Class<?> elementClass = getGenericElementType(field);
                            Object elementValue = null;
                            if (elementClass == String.class) {
                                elementValue = message.getBody()
                                        .getBlockMap(targetBlockIdx)
                                        .get(DaxCoreTags.COLLECTION_VALUE)
                                        .getStrValue();
                            }
                            else {

                                elementValue = createFromMessage(message, elementClass, targetBlockIdx);
                            }



                            collection.add(elementValue);

                        }



                        field.set(instance, collection);
                    }
                    else{

                      System.out.println("TODO  > "+ tagCodec.encode(tag)+ " refBlocksIdx = "+refBlocksIdx);
                        if (!refBlocksIdx.isEmpty()) {
                            int targetBlockIdx = refBlocksIdx.iterator().next() - 1;
                            Object nestedObject = createFromMessage(message, field.getType(), targetBlockIdx);
                            field.set(instance, nestedObject);
                        }
                    }

                }


            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map DAXP to " + targetClass.getSimpleName(), e);
        }
    }
    @SuppressWarnings("rawtypes")
    private Collection<Object> createCollectionInstance(Class<?> fieldType) {
        if (fieldType.isAssignableFrom(ArrayList.class)) return new ArrayList<>();
        if (fieldType.isAssignableFrom(LinkedList.class)) return new LinkedList<>();
        if (fieldType.isAssignableFrom(HashSet.class)) return new HashSet<>();
        if (fieldType.isAssignableFrom(LinkedHashSet.class)) return new LinkedHashSet<>();
        if (fieldType.isAssignableFrom(TreeSet.class)) return new TreeSet<>();

        // Fallback based on interface defaults
        if (Set.class.isAssignableFrom(fieldType)) return new LinkedHashSet<>();
        if (List.class.isAssignableFrom(fieldType)) return new LinkedList<>(); // Prioritizes LinkedList per your setup

        try {
            Constructor<?> constructor = fieldType.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (Collection<Object>) constructor.newInstance();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    private Class<?> getGenericElementType(Field field) {
        Type genericFieldType = field.getGenericType();
        if (genericFieldType instanceof ParameterizedType) {
            ParameterizedType aType = (ParameterizedType) genericFieldType;
            Type[] fieldArgTypes = aType.getActualTypeArguments();
            if (fieldArgTypes.length > 0 && fieldArgTypes[0] instanceof Class) {
                return (Class<?>) fieldArgTypes[0];
            }
        }
        return String.class; // default fallback if untyped
    }
    /**
     * Instantiates the correct Map implementation (HashMap, TreeMap, LinkedHashMap, etc.)
     */
    @SuppressWarnings("rawtypes")
    private Map<Object, Object> createMapInstance(Class<?> fieldType) {
        if (fieldType.isAssignableFrom(HashMap.class)) return new HashMap<>();
        if (fieldType.isAssignableFrom(LinkedHashMap.class)) return new LinkedHashMap<>();
        if (fieldType.isAssignableFrom(TreeMap.class)) return new TreeMap<>();

        if (Map.class.isAssignableFrom(fieldType)) return new LinkedHashMap<>(); // Sensible default preserving order

        try {
            Constructor<?> constructor = fieldType.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (Map<Object, Object>) constructor.newInstance();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }


      public  void updateFromMessage(DaxMessage message, Object obj){
        Class<?> clazz = obj.getClass();
        try {

        for (Field f : clazz.getDeclaredFields()) {
            DaxpField ann = f.getAnnotation(DaxpField.class);
            if (ann == null) continue;

            int contextId = config.getAppContextId() ;

            DaxTag tag = DaxTag.of(contextId , ann.tagId());
            if(! message.getBody().getBlock(0).containsKey(tag)) continue;

            var pair = message.get(0,tag);

            if (pair==null) continue; // gracefully ignore missing tags or empty

            String raw = pair.getStrValue();
            Object converted = dataTypeCodec.convert(raw, f.getType());  // if not ..convert from dictionary

            f.setAccessible(true);
            f.set(obj, converted);
        }
    } catch (Exception e) {
        throw new RuntimeException("Failed to map DAXP to " + clazz.getSimpleName(), e);
    }
 }
}