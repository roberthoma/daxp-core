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

package org.daxprotocol.core.conventer;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.codec.DaxDecodeService;
import org.daxprotocol.core.config.DaxpConfig;
import org.daxprotocol.core.mapper.DaxReferenceMapper;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.tag.DaxTag;

import java.lang.reflect.Field;

public class DaxMessageConverter {

    DaxpConfig config;
    DaxReferenceMapper contextMapper;

    public DaxMessageConverter(DaxpConfig config, DaxReferenceMapper contextMapper) {
        this.config = config;
        this.contextMapper = contextMapper;
    }

    public <T> T createFromMessage(DaxMessage message, Class<T> targetClass) {
        try {
            T instance = targetClass.getDeclaredConstructor().newInstance();

            for (Field f : targetClass.getDeclaredFields()) {
                DaxpField ann = f.getAnnotation(DaxpField.class);
                if (ann == null) continue; // skip non-annotated fields (e.g., town)

                int contextId = ann.context().isBlank() ? config.getAppContextId():
                        contextMapper.getReferenceId(ann.context());

                var pair = message.get(new DaxTag( contextId, ann.tagId()));
                if (pair==null) continue; // gracefully ignore missing tags or empty

                String raw = pair.getStrValue();
                Object converted = DaxDecodeService.convert(raw, f.getType());  // if not ..convert from dictionary

                f.setAccessible(true);
                f.set(instance, converted);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map DAXP to " + targetClass.getSimpleName(), e);
        }
    }



      public  void updateFromMessage(DaxMessage message, Object obj){
        Class<?> clazz = obj.getClass();
        try {

        for (Field f : clazz.getDeclaredFields()) {
            DaxpField ann = f.getAnnotation(DaxpField.class);
            if (ann == null) continue;

            int contextId = config.getAppContextId() ;

            DaxTag tag = new DaxTag(contextId , ann.tagId());
            if(! message.getBody().getBlock(0).containsKey(tag)) continue;

            var pair = message.get(tag);

            if (pair==null) continue; // gracefully ignore missing tags or empty

            String raw = pair.getStrValue();
            Object converted = DaxDecodeService.convert(raw, f.getType());  // if not ..convert from dictionary

            f.setAccessible(true);
            f.set(obj, converted);
        }
    } catch (Exception e) {
        throw new RuntimeException("Failed to map DAXP to " + clazz.getSimpleName(), e);
    }
 }
}