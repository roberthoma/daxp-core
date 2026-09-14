package org.daxprotocol.core.test.dax_01_unit_test.dax_010_DaxReferenceMapper;

import org.daxprotocol.core.mapper.DaxReference;
import org.daxprotocol.core.mapper.DaxReferenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DaxReferenceMapperTest {

    private DaxReferenceMapper<String> mapper;

    @BeforeEach
    void setUp() {
        mapper = new DaxReferenceMapper<>();
    }

    /**
     * Helper method to create a mocked org.daxprotocol.core.mapper.DaxReference
     */
    @SuppressWarnings("unchecked")
    private DaxReference<String> mockDaxReference(String reference, int id) {
        DaxReference<String> ref = Mockito.mock(DaxReference.class);
        when(ref.getReference()).thenReturn(reference);
        when(ref.getId()).thenReturn(id);
        return ref;
    }

    @Nested
    @DisplayName("Predefined Registration Tests")
    class PredefinedRegistrationTests {

        @Test
        @DisplayName("Should successfully register predefined reference with ID lower than beginId")
        void testRegisterPredefinedSuccess() {
            DaxReferenceMapper<String> customMapper = new DaxReferenceMapper<>(100);

            customMapper.registerPredefined("FIX", 10);

            assertEquals(10, customMapper.getReferenceId("FIX"));
            assertEquals("FIX", customMapper.getReference(10));
            assertTrue(customMapper.isKnownSymbol("FIX"));
            assertTrue(customMapper.isKnownId(10));
        }

        @Test
        @DisplayName("Should successfully register predefined via DaxReference object")
        void testRegisterPredefinedObjectSuccess() {
            DaxReferenceMapper<String> customMapper = new DaxReferenceMapper<>(100);
            DaxReference<String> ref = mockDaxReference("CRM", 50);

            customMapper.registerPredefined(ref);

            assertEquals(50, customMapper.getReferenceId("CRM"));
            assertEquals("CRM", customMapper.getReference(50));
        }

        @Test
        @DisplayName("Should throw exception when registering predefined ID greater than or equal to beginId")
        void testRegisterPredefinedThrowsExceptionWhenIdInvalid() {
            DaxReferenceMapper<String> customMapper = new DaxReferenceMapper<>(10);
            DaxReference<String> refInvalidEquals = mockDaxReference("TEST1", 10);
            DaxReference<String> refInvalidGreater = mockDaxReference("TEST2", 15);

            RuntimeException ex1 = assertThrows(RuntimeException.class,
                    () -> customMapper.registerPredefined(refInvalidEquals));
            assertTrue(ex1.getMessage().contains("greater than 10"));

            RuntimeException ex2 = assertThrows(RuntimeException.class,
                    () -> customMapper.registerPredefined(refInvalidGreater));
            assertTrue(ex2.getMessage().contains("greater than 10"));
        }
    }

    @Nested
    @DisplayName("Dynamic Reference Mapping Tests")
    class DynamicMappingTests {

        @Test
        @DisplayName("Should generate sequential IDs starting from beginId for unknown symbols")
        void testDynamicRegistrationSequential() {
            int id1 = mapper.getReferenceId("FIX");
            int id2 = mapper.getReferenceId("CRM");

            assertEquals(1, id1);
            assertEquals(2, id2);
            assertEquals("FIX", mapper.getReference(1));
            assertEquals("CRM", mapper.getReference(2));
        }

        @Test
        @DisplayName("Should return existing ID when requesting an already registered reference")
        void testGetReferenceIdExistingSymbol() {
            int firstCall = mapper.getReferenceId("FIX");
            int secondCall = mapper.getReferenceId("FIX");

            assertEquals(firstCall, secondCall);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when passed null reference")
        void testGetReferenceIdNullThrowsException() {
            assertThrows(IllegalArgumentException.class, () -> mapper.getReferenceId(null));
        }

        @Test
        @DisplayName("Should return null for unknown ID lookup")
        void testGetReferenceUnknownId() {
            assertNull(mapper.getReference(999));
        }
    }

    @Nested
    @DisplayName("Status & Inspection API Tests")
    class StatusAndInspectionTests {

        @Test
        @DisplayName("Should correctly identify known and unknown symbols/IDs")
        void testIsKnownSymbolAndId() {
            assertFalse(mapper.isKnownSymbol("FIX"));
            assertFalse(mapper.isKnownId(1));

            mapper.getReferenceId("FIX");

            assertTrue(mapper.isKnownSymbol("FIX"));
            assertTrue(mapper.isKnownId(1));
            assertFalse(mapper.isKnownSymbol(null));
        }

        @Test
        @DisplayName("getAllMappings should return a copy of all current mappings")
        void testGetAllMappings() {
            mapper.getReferenceId("FIX");
            mapper.getReferenceId("CRM");

            Map<String, Integer> mappings = mapper.getAllMappings();

            assertEquals(2, mappings.size());
            assertEquals(1, mappings.get("FIX"));
            assertEquals(2, mappings.get("CRM"));

            mappings.put("NEW", 99);
            assertFalse(mapper.isKnownSymbol("NEW"));
        }
    }

    @Nested
    @DisplayName("Concurrency & Thread Safety Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Should safely assign unique IDs when accessed by multiple threads simultaneously")
        void testConcurrentDynamicRegistration() throws InterruptedException {
            int threadCount = 20;
            int itemsPerThread = 100;
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(1);

            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    try {
                        latch.await();
                        for (int j = 0; j < itemsPerThread; j++) {
                            String symbol = "SYMBOL_" + threadId + "_" + j;
                            mapper.getReferenceId(symbol);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            latch.countDown();
            executor.shutdown();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));

            Map<String, Integer> mappings = mapper.getAllMappings();
            assertEquals(threadCount * itemsPerThread, mappings.size());

            AtomicInteger nullCount = new AtomicInteger(0);
            mappings.forEach((symbol, id) -> {
                String fetchedSymbol = mapper.getReference(id);
                if (fetchedSymbol == null) {
                    nullCount.incrementAndGet();
                }
            });

            assertEquals(0, nullCount.get(), "Bidirectional lookup failed for some thread-generated IDs");
        }
    }
}