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
package org.daxprotocol.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/***************************************
 *  Field is value of class
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface DaxpField {
    String value() default "";      //context plus tagId "FIX:53"
    int tagId() default -1;                   // It can be define by @DaxpTag
    String context() default "";   // Empty mean  DaxpConfig.APP_CONTEXT_SYMBOL;
    String uiLabel() default "";
    String name() default "";      //Use for rename field name , example :used for JSON cast.
    String description() default "";
}
