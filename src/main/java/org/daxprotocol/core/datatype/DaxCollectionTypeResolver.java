/************************************************************************
 * DAXP – Data & Attribute eXchange Protocol
 * Copyright 2026 DAXPARC Robert Homa
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
package org.daxprotocol.core.datatype;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Resolves inherited generic arguments against Collection or Map. */
public final class DaxCollectionTypeResolver {

    /**
     * Returns a resolved Collection<E> or Map<K,V>, or null when concrete
     * arguments cannot be determined. Wildcards are deliberately unresolved.
     * This method describes declared types, not the contents of an instance.
     */
    public Type resolve(Type source, Class<?> contract) {
        Objects.requireNonNull(source, "source");
        if (contract != Collection.class && contract != Map.class) {
            throw new IllegalArgumentException("Contract must be Collection.class or Map.class");
        }
        return resolve(source, contract, Map.of());
    }

    private Type resolve(Type source, Class<?> contract,
                         Map<TypeVariable<?>, Type> inherited) {
        Class<?> raw;
        Map<TypeVariable<?>, Type> bindings = new HashMap<>(inherited);
        if (source instanceof ParameterizedType parameterized) {
            raw = (Class<?>) parameterized.getRawType();
            bind(parameterized, bindings);
        } else if (source instanceof Class<?> clazz) {
            raw = clazz;
        } else {
            return null;
        }

        if (!contract.isAssignableFrom(raw)) return null;
        if (raw == contract) {
            Type[] arguments = Arrays.stream(raw.getTypeParameters())
                    .map(variable -> substitute(variable, bindings))
                    .toArray(Type[]::new);
            return Arrays.stream(arguments).allMatch(this::isConcrete)
                    ? new ResolvedType(raw, null, arguments) : null;
        }

        Type result = null;
        for (Type parent : raw.getGenericInterfaces()) {
            result = merge(result, resolve(parent, contract, bindings));
        }
        Type superclass = raw.getGenericSuperclass();
        if (superclass != null) {
            result = merge(result, resolve(superclass, contract, bindings));
        }
        return result;
    }

    private void bind(ParameterizedType type, Map<TypeVariable<?>, Type> bindings) {
        if (type.getOwnerType() instanceof ParameterizedType owner) bind(owner, bindings);
        Class<?> raw = (Class<?>) type.getRawType();
        TypeVariable<?>[] variables = raw.getTypeParameters();
        Type[] arguments = type.getActualTypeArguments();
        for (int i = 0; i < variables.length; i++) {
            Type argument = substitute(arguments[i], bindings);
            if (!variables[i].equals(argument)) bindings.put(variables[i], argument);
        }
    }

    private Type substitute(Type type, Map<TypeVariable<?>, Type> bindings) {
        if (type instanceof TypeVariable<?> variable) {
            Type bound = bindings.get(variable);
            return bound == null || bound.equals(variable) ? variable : substitute(bound, bindings);
        }
        if (type instanceof ParameterizedType parameterized) {
            Type owner = parameterized.getOwnerType();
            Type[] arguments = Arrays.stream(parameterized.getActualTypeArguments())
                    .map(argument -> substitute(argument, bindings)).toArray(Type[]::new);
            return new ResolvedType((Class<?>) parameterized.getRawType(),
                    owner == null ? null : substitute(owner, bindings), arguments);
        }
        if (type instanceof GenericArrayType array) {
            Type component = substitute(array.getGenericComponentType(), bindings);
            if (component instanceof Class<?> clazz) return Array.newInstance(clazz, 0).getClass();
            return new ResolvedArrayType(component);
        }
        return type;
    }

    private boolean isConcrete(Type type) {
        if (type instanceof TypeVariable<?> || type instanceof WildcardType) return false;
        if (type instanceof ParameterizedType parameterized) {
            return (parameterized.getOwnerType() == null || isConcrete(parameterized.getOwnerType()))
                    && Arrays.stream(parameterized.getActualTypeArguments()).allMatch(this::isConcrete);
        }
        if (type instanceof GenericArrayType array) return isConcrete(array.getGenericComponentType());
        return type instanceof Class<?>;
    }

    private Type merge(Type first, Type next) {
        if (first == null) return next;
        if (next != null && !first.equals(next)) {
            throw new IllegalArgumentException("Conflicting collection types: " + first + " and " + next);
        }
        return first;
    }

    private record ResolvedArrayType(Type component) implements GenericArrayType {
        @Override public Type getGenericComponentType() { return component; }
        @Override public String getTypeName() { return component.getTypeName() + "[]"; }
        @Override public boolean equals(Object other) {
            return other instanceof GenericArrayType array
                    && component.equals(array.getGenericComponentType());
        }
        @Override public int hashCode() { return component.hashCode(); }
    }

    private static final class ResolvedType implements ParameterizedType {
        private final Class<?> raw;
        private final Type owner;
        private final Type[] arguments;

        private ResolvedType(Class<?> raw, Type owner, Type[] arguments) {
            this.raw = raw;
            this.owner = owner;
            this.arguments = arguments.clone();
        }

        @Override public Type[] getActualTypeArguments() { return arguments.clone(); }
        @Override public Type getRawType() { return raw; }
        @Override public Type getOwnerType() { return owner; }
        @Override public boolean equals(Object other) {
            return other instanceof ParameterizedType type
                    && raw.equals(type.getRawType())
                    && Objects.equals(owner, type.getOwnerType())
                    && Arrays.equals(arguments, type.getActualTypeArguments());
        }
        @Override public int hashCode() {
            return Arrays.hashCode(arguments) ^ Objects.hashCode(owner) ^ raw.hashCode();
        }
        @Override public String getTypeName() {
            return raw.getTypeName() + "<" + String.join(", ",
                    Arrays.stream(arguments).map(Type::getTypeName).toList()) + ">";
        }
        @Override public String toString() { return getTypeName(); }
    }
}
