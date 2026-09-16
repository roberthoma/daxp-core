package org.daxprotocol.core.test.dax_01_unit_test.dax_00_10_DataType;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.datatype.DaxCollectionInfo;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.datatype.DaxDataTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.daxprotocol.core.test.dax_00_service.DaxTestLogger.printLog;

public class DaxDataTypeServiceTest {

    enum TestEnum {
        TEST_ENUM1,
        TEST_ENUM2
    }

    @DaxpEntity
    static class SampleDaxpEntity {
        private String name;
    }

    static class PlainObject {
        private String id;
    }

    private DaxDataTypeService service;
    private TestEnum testInsEnum;

    @BeforeEach
    void setUp() {
        service = new DaxDataTypeService();
        testInsEnum = TestEnum.TEST_ENUM1;
    }

    @Test
    @DisplayName("isDaxpCollection(Object) should return true for collections and false for other types")
    void isDaxpCollectionObject() {
        printLog("Executing isDaxpCollectionObject test");

        Map<Integer, String> testMap = new HashMap<>();
        testMap.put(1, "Test value");

        assertThat(service.isDaxpCollection(testMap)).isTrue();
        assertThat(service.isDaxpCollection(List.of("a", "b"))).isTrue();
        assertThat(service.isDaxpCollection(Set.of("a"))).isTrue();
        assertThat(service.isDaxpCollection("Tekst")).isFalse();
        assertThat(service.isDaxpCollection(123)).isFalse();
        assertThat(service.isDaxpCollection(TestEnum.class)).isTrue();
        assertThat(service.isDaxpCollection(testInsEnum)).isFalse(); // Enum instance decodes as STRING, not COLLECTION

        printLog("Finished isDaxpCollectionObject test");
    }

    @Test
    @DisplayName("isObjInstanceOfCollection should correctly identify collection instances")
    void isObjInstanceOfCollection() {
        printLog("Executing isObjInstanceOfCollection test");

        assertThat(service.isObjInstanceOfCollection(new ArrayList<>())).isTrue();
        assertThat(service.isObjInstanceOfCollection(new HashMap<>())).isTrue();
        assertThat(service.isObjInstanceOfCollection(new HashSet<>())).isTrue();
        assertThat(service.isObjInstanceOfCollection("String")).isFalse();
        assertThat(service.isObjInstanceOfCollection(null)).isFalse();
        assertThat(service.isObjInstanceOfCollection(testInsEnum)).isFalse();

        printLog("Finished isObjInstanceOfCollection test");
    }

    @Test
    @DisplayName("isDaxpCollection(Class) should correctly verify collection, map, and enum classes")
    void isDaxpCollectionClass() {
        printLog("Executing isDaxpCollectionClass test");

        assertThat(service.isDaxpCollection(List.class)).isTrue();
        assertThat(service.isDaxpCollection(ArrayList.class)).isTrue();
        assertThat(service.isDaxpCollection(Map.class)).isTrue();
        assertThat(service.isDaxpCollection(HashMap.class)).isTrue();
        assertThat(service.isDaxpCollection(Set.class)).isTrue();
        assertThat(service.isDaxpCollection(TestEnum.class)).isTrue();

        assertThat(service.isDaxpCollection(String.class)).isFalse();
        assertThat(service.isDaxpCollection(Integer.class)).isFalse();
        assertThat(service.isDaxpCollection((Class<?>) null)).isFalse();

        printLog("Finished isDaxpCollectionClass test");
    }

    @Test
    @DisplayName("decodeFromObject should correctly recognize data types based on instances")
    void decodeFromObject() {
        printLog("Executing decodeFromObject test");

        assertThat(service.decodeFromObject(null)).isEqualTo(DaxDataType.NONE);
        assertThat(service.decodeFromObject("Text")).isEqualTo(DaxDataType.STRING);
        assertThat(service.decodeFromObject(testInsEnum)).isEqualTo(DaxDataType.STRING);
        assertThat(service.decodeFromObject(100)).isEqualTo(DaxDataType.INTEGER);
        assertThat(service.decodeFromObject(50L)).isEqualTo(DaxDataType.LONG);
        assertThat(service.decodeFromObject(new ArrayList<>())).isEqualTo(DaxDataType.COLLECTION);
        assertThat(service.decodeFromObject(new SampleDaxpEntity())).isEqualTo(DaxDataType.ENTITY);

        printLog("Finished decodeFromObject test");
    }

    @Test
    @DisplayName("decodeClass should return the correct DaxDataType for known classes")
    void decodeClass() {
        printLog("Executing decodeClass test");

        assertThat(service.decodeClass((Class<?>) null)).isEqualTo(DaxDataType.NONE);
        assertThat(service.decodeClass(String.class)).isEqualTo(DaxDataType.STRING);
        assertThat(service.decodeClass(Integer.class)).isEqualTo(DaxDataType.INTEGER);
        assertThat(service.decodeClass(int.class)).isEqualTo(DaxDataType.INTEGER);
        assertThat(service.decodeClass(Long.class)).isEqualTo(DaxDataType.LONG);
        assertThat(service.decodeClass(BigDecimal.class)).isEqualTo(DaxDataType.DECIMAL);
        assertThat(service.decodeClass(Double.class)).isEqualTo(DaxDataType.DOUBLE);
        assertThat(service.decodeClass(Boolean.class)).isEqualTo(DaxDataType.BOOLEAN);
        assertThat(service.decodeClass(LocalDate.class)).isEqualTo(DaxDataType.LOCAL_DATE);
        assertThat(service.decodeClass(LocalDateTime.class)).isEqualTo(DaxDataType.LOCAL_DATE_TIME);
        assertThat(service.decodeClass(Character.class)).isEqualTo(DaxDataType.CHARACTER);
        assertThat(service.decodeClass(char.class)).isEqualTo(DaxDataType.CHARACTER);

        assertThat(service.decodeClass(List.class)).isEqualTo(DaxDataType.COLLECTION);
        assertThat(service.decodeClass(Map.class)).isEqualTo(DaxDataType.COLLECTION);
        assertThat(service.decodeClass(SampleDaxpEntity.class)).isEqualTo(DaxDataType.ENTITY);
        assertThat(service.decodeClass(Enum.class)).isEqualTo(DaxDataType.STRING);
        assertThat(service.decodeClass(PlainObject.class)).isEqualTo(DaxDataType.UNKNOWN);

        printLog("Finished decodeClass test");
    }

    @Test
    @DisplayName("isPrimitiveType should return true for primitives, wrappers, and core value types")
    void isPrimitiveType() {
        printLog("Executing isPrimitiveType test");

        assertThat(service.isPrimitiveType(String.class)).isTrue();
        assertThat(service.isPrimitiveType(Integer.class)).isTrue();
        assertThat(service.isPrimitiveType(int.class)).isTrue();
        assertThat(service.isPrimitiveType(char.class)).isTrue();
        assertThat(service.isPrimitiveType(Character.class)).isTrue();
        assertThat(service.isPrimitiveType(Long.class)).isTrue();
        assertThat(service.isPrimitiveType(BigDecimal.class)).isTrue();
        assertThat(service.isPrimitiveType(Double.class)).isTrue();
        assertThat(service.isPrimitiveType(Boolean.class)).isTrue();
        assertThat(service.isPrimitiveType(LocalDate.class)).isTrue();
        assertThat(service.isPrimitiveType(LocalDateTime.class)).isTrue();

        assertThat(service.isPrimitiveType(Object.class)).isFalse();
        assertThat(service.isPrimitiveType(PlainObject.class)).isFalse();

        printLog("Finished isPrimitiveType test");
    }

    @Test
    @DisplayName("castReflectTypeToClass should correctly cast Type objects to Class")
    void castReflectTypeToClass() {
        printLog("Executing castReflectTypeToClass test");

        // Standard class reflection
        Type stringType = String.class;
        assertThat(service.castReflectTypeToClass(stringType)).isEqualTo(String.class);

        // Parameterized type reflection (e.g., List<String>)
        ParameterizedType listType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        assertThat(service.castReflectTypeToClass(listType)).isEqualTo(List.class);
        assertThat(service.castReflectTypeToClass(null)).isNull();

        printLog("Finished castReflectTypeToClass test");
    }

    @Test
    @DisplayName("getCollectionInfo should populate metadata flags for supported collection structures")
    void getCollectionInfo() {
        printLog("Executing getCollectionInfo test");

        // Set
        DaxCollectionInfo setInfo = service.getCollectionInfo(Set.class);
        assertThat(setInfo.isCollection).isTrue();

        // List
        DaxCollectionInfo listInfo = service.getCollectionInfo(List.class);
        assertThat(listInfo.isColAllowDuplicates).isTrue();
        assertThat(listInfo.isCollection).isTrue();

        // Map
        DaxCollectionInfo mapInfo = service.getCollectionInfo(Map.class);
        assertThat(mapInfo.isCollection).isTrue();
        assertThat(mapInfo.isColHasKey).isTrue();

        // Enum
        DaxCollectionInfo enumInfo = service.getCollectionInfo(TestEnum.class);
        assertThat(enumInfo.isCollection).isTrue();
        assertThat(enumInfo.isColHasKey).isTrue();
        assertThat(enumInfo.isColDictionary).isTrue();
        assertThat(enumInfo.isJavaEnum).isTrue();
        assertThat(enumInfo.isClosed).isTrue();

        // LinkedList (Navigable)
        DaxCollectionInfo linkedListInfo = service.getCollectionInfo(LinkedList.class);
        assertThat(linkedListInfo.isCollection).isTrue();
        assertThat(linkedListInfo.isColNavigable).isTrue();

        printLog("Finished getCollectionInfo test");
    }

    @Test
    @DisplayName("getCollectionInfo should throw an exception for non-collection classes")
    void getCollectionInfoThrowsExceptionForNonCollection() {
        printLog("Executing getCollectionInfoThrowsExceptionForNonCollection test");

        assertThatThrownBy(() -> service.getCollectionInfo(String.class))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("It is NOT COLLECTION !!!");

        printLog("Finished getCollectionInfoThrowsExceptionForNonCollection test");
    }

    @Test
    @DisplayName("isMap should return true only for Map instances and false otherwise")
    void isMap() {
        printLog("Executing isMap test");

        Map<String, String> mapInstance = new HashMap<>();
        assertThat(service.isMap(mapInstance)).isTrue();

        List<String> listInstance = new ArrayList<>();
        assertThat(service.isMap(listInstance)).isFalse();

        printLog("Finished isMap test");
    }
}