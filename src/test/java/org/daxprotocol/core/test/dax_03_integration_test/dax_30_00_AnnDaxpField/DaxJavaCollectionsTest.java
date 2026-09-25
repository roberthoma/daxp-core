package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpCollection;
import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.tag.DaxTag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DaxJavaCollectionsTest extends DaxConfigBaseInit {

    @Test
    void checkSetCollection() {

        @DaxpEntity(tagId = 1000)
        class TestClass01 {
            @DaxpField(tagId = 1001) Set<String> strList;
        }
        daxEngine.register(TestClass01.class);
        printSemanticRegister();

        DaxTag tag1001 = DaxTag.of(config.getAppNamespaceId(), 1001);

        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
        Assertions.assertFalse(semanticInspector.hasKey(tag1001));
    }

    @Test
    void checkListCollection() {
        @DaxpEntity(tagId = 1000)
        class TestClass01 {
            @DaxpField(tagId = 1001) List<String> strList;
        }
        daxEngine.register(TestClass01.class);
        printSemanticRegister();
        DaxTag tag1001 = DaxTag.of(config.getAppNamespaceId(), 1001);

        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
        Assertions.assertFalse(semanticInspector.hasKey(tag1001));
        Assertions.assertTrue(semanticInspector.isAllowDuplicates(tag1001));
    }

    @Test
    void checkMapCollection() {
        @DaxpEntity(tagId = 1000)
        class TestClass01 {
            @DaxpField(tagId = 1001) Map<Integer, String> strMap;
        }
        daxEngine.register(TestClass01.class);
        printSemanticRegister();
        DaxTag tag1001 = DaxTag.of(config.getAppNamespaceId(), 1001);

        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
        Assertions.assertEquals(DaxDataType.INTEGER, semanticInspector.getKeyDataType(tag1001));
        Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1001));
        Assertions.assertTrue(semanticInspector.hasKey(tag1001));
    }

    @Test
    void checkImplOfSetCollection() {
        @DaxpCollection(tagId = 1010)
        class TestSet implements Set<String>{

            @Override public int size() {
                return 0;
            }

            @Override public boolean isEmpty() {
                return false;
            }

            @Override public boolean contains(Object o) {
                return false;
            }

            @Override public Iterator<String> iterator() {
                return null;
            }

            @Override public Object[] toArray() {
                return new Object[0];
            }

            @Override public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override public boolean add(String s) {
                return false;
            }

            @Override public boolean remove(Object o) {
                return false;
            }

            @Override public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override public boolean addAll(Collection<? extends String> c) {
                return false;
            }

            @Override public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override public void clear() {

            }
        }

        @DaxpEntity(tagId = 1000)
        class TestClass01 {
            @DaxpField(tagId = 1001) TestSet strList;
        }
        daxEngine.register(TestSet.class);
        daxEngine.register(TestClass01.class);

        printSemanticRegister();

        DaxTag tag1001 = DaxTag.of(config.getAppNamespaceId(), 1001);
        DaxTag tag1010 = DaxTag.of(config.getAppNamespaceId(), 1010);

//        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));
   //     Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1001));  reference

         Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(tag1010));
         Assertions.assertEquals(DaxDataType.STRING, semanticInspector.getCollectionValueDataType(tag1010));
    }




}