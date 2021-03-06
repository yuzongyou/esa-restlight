/*
 * Copyright 2020 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esa.restlight.core.annotation;

import java.lang.annotation.*;

/**
 * Indicates that all the {@link java.lang.reflect.Field}s of the parameter annotated by this annotation
 * will be resolved one bye one if the {@link java.lang.reflect.Field} which would be transfer to an implementation of
 * {@link esa.restlight.core.method.Param} can be matched to an {@link esa.restlight.core.resolver.ArgumentResolver}
 * just like a simple method argument.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBean {
}
