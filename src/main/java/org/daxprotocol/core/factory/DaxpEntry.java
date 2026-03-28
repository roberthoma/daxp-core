package org.daxprotocol.core.factory;

import org.daxprotocol.core.annotation.DaxpDTO;

import java.lang.reflect.Field;

/**
 * Represents a specific field mapping during DAXP serialization.
 * @param entry The object instance (e.g., a Customer or Address object)
 * @param field The reflection metadata for the specific field
 */
public record DaxpEntry(Object entry, Field field) {

        // You can add a helper method to get the value easily
        public Object getValue() throws IllegalAccessException {
                field.setAccessible(true);
                return field.get(entry);
        }

        // Helper to check if the field is another DAXP DTO
        public boolean isComplex() {
                return field.getType().isAnnotationPresent(DaxpDTO.class);
        }
}