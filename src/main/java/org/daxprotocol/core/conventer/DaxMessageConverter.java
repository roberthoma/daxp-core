/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2025 Robert Homa
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
import org.daxprotocol.core.annotation.DaxpTag;
import org.daxprotocol.core.codec.DaxDecodeService;
import org.daxprotocol.core.model.DaxMessage;
import java.lang.reflect.Field;

public class DaxMessageConverter {


    public static <T> T fromMessage(DaxMessage message, Class<T> targetClass) {
        try {
            T instance = targetClass.getDeclaredConstructor().newInstance();

            for (Field f : targetClass.getDeclaredFields()) {
                DaxpTag ann = f.getAnnotation(DaxpTag.class);
                if (ann == null) continue; // skip non-annotated fields (e.g., town)

                int tag = ann.tag();
                var maybeVal = message.get(tag);
                if (maybeVal==null) continue; // gracefully ignore missing tags or empty

                String raw = maybeVal.getStrValue();
                Object converted = DaxDecodeService.convert(raw, f.getType());  // if not ..convert from dictionary

                f.setAccessible(true);
                f.set(instance, converted);
            }
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map DAXP to " + targetClass.getSimpleName(), e);
        }
    }


}


//TODO Dictionary conventer