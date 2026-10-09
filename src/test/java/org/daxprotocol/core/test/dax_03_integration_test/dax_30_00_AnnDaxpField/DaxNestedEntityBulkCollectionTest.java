package org.daxprotocol.core.test.dax_03_integration_test.dax_30_00_AnnDaxpField;

import org.daxprotocol.core.annotation.DaxpEntity;
import org.daxprotocol.core.annotation.DaxpField;
import org.daxprotocol.core.datatype.DaxDataType;
import org.daxprotocol.core.model.DaxMessage;
import org.daxprotocol.core.model.preamble.DaxPreamble;
import org.daxprotocol.core.model.tag.DaxTag;
import org.daxprotocol.core.test.dax_00_service.DaxMessageDecorator;
import org.daxprotocol.core.test.dax_03_integration_test.dax_00_00_base.DaxNamespaceConfigBaseInit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

public class DaxNestedEntityBulkCollectionTest extends DaxNamespaceConfigBaseInit {

    @Test
    @DisplayName("Verify Set collection containing nested entities and bulk size configuration")
    void checkSetEntityCollection() {
        System.out.println("------------------------------------");
        System.out.println("checkSetEntityCollection");
        System.out.println("------------------------------------");

        @DaxpEntity(tagId = 900)
        class EngineSpec {
            @DaxpField(tagId = 902) String engineType;
            @DaxpField(tagId = 903, namespace = "FIX") String fuelType;
            @DaxpField(tagId = 904) Integer horsepower;

            public EngineSpec(String engineType, String fuelType) {
                this.engineType = engineType;
                this.fuelType = fuelType;
            }

            public EngineSpec(String engineType, String fuelType, Integer horsepower) {
                this.engineType = engineType;
                this.fuelType = fuelType;
                this.horsepower = horsepower;
            }
        }

        @DaxpEntity(tagId = 1000)
        class Vehicle {
            @DaxpField(tagId = 1002) String model;
            @DaxpField(tagId = 1003) EngineSpec engine;

            public Vehicle(String model, String engineType) {
                this.model = model;
                this.engine = new EngineSpec(engineType, "Gasoline", 300);
            }

            public Vehicle(String model, String engineType, Integer horsepower) {
                this.model = model;
                this.engine = new EngineSpec(engineType, "Diesel", horsepower);
            }
        }

        @DaxpEntity(tagId = 1010)
        class FleetManifest {
            @DaxpField(tagId = 1011) final Set<Vehicle> vehicles = new HashSet<>();
            @DaxpField(tagId = 1012) String fleetName;

            public FleetManifest() {
                this.vehicles.add(new Vehicle("BMW M3", "3.0L TwinTurbo"));
                this.vehicles.add(new Vehicle("Audi RS6", "4.0L V8", 600));
                this.vehicles.add(new Vehicle("Porsche 911", "3.8L Flat-6"));
                this.vehicles.add(new Vehicle("Mercedes AMG GT", "4.0L Biturbo", 523));
                this.fleetName = "Executive Fleet";
            }
        }

        daxEngine.register(EngineSpec.class);
        daxEngine.register(Vehicle.class);
        daxEngine.register(FleetManifest.class);

        printSemanticRegister();

        FleetManifest manifest = new FleetManifest();
        DaxPreamble preamble = new DaxPreamble();

        // 1. Encode with minimum bulk threshold set to 100
        daxEngine.getCoreConfig().setMinBulkSize(100);
        DaxMessage msg1 = msgFactory.toDaxMessage("BULK.FLEET", manifest);
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg1, preamble)));

        // 2. Encode with default bulk threshold
        daxEngine.getCoreConfig().resetMinBulkSize();
        DaxMessage msg2 = msgFactory.toDaxMessage("BULK.FLEET", manifest);
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg2, preamble)));

        DaxTag collectionTag = DaxTag.of(config.getAppNamespaceId(), 1011);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(collectionTag));
        System.out.println("----------------------------------------");
    }

    @Test
    @DisplayName("Verify List collection containing entity records")
    void checkListEntityCollection() {
        System.out.println("------------------------------------");
        System.out.println("checkListEntityCollection");
        System.out.println("------------------------------------");

        @DaxpEntity(tagId = 1000)
        class CarModel {
            @DaxpField(tagId = 1002) String brand;
            @DaxpField(tagId = 1003) String model;
            @DaxpField(tagId = 1004) Integer productionYear;

            public CarModel(String brand, String model) {
                this.brand = brand;
                this.model = model;
            }

            public CarModel(String brand, String model, Integer productionYear) {
                this.brand = brand;
                this.model = model;
                this.productionYear = productionYear;
            }
        }

        @DaxpEntity(tagId = 1010)
        class BrandCatalog {
            @DaxpField(tagId = 1011) final List<CarModel> carList = new ArrayList<>();
            @DaxpField(tagId = 1012) String catalogName;

            public BrandCatalog() {
                this.carList.add(new CarModel("Toyota", "Corolla"));
                this.carList.add(new CarModel("Ford", "Mustang", 2024));
                this.carList.add(new CarModel("Honda", "Civic Type R"));
                this.carList.add(new CarModel("Chevrolet", "Corvette", 2023));
                this.catalogName = "Global Vehicles Catalog";
            }
        }

        daxEngine.register(CarModel.class);
        daxEngine.register(BrandCatalog.class);

        printSemanticRegister();

        BrandCatalog catalog = new BrandCatalog();
        DaxMessage msg = msgFactory.toDaxMessage("BULK.CATALOG", catalog);
        DaxPreamble preamble = new DaxPreamble();

        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));

        DaxTag collectionTag = DaxTag.of(config.getAppNamespaceId(), 1011);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(collectionTag));
        System.out.println("----------------------------------------");
    }

    @Test
    @DisplayName("Verify Map collection with primitive keys and entity values")
    void checkMapEntityCollection() {
        System.out.println("------------------------------------");
        System.out.println("checkMapEntityCollection");
        System.out.println("------------------------------------");

        @DaxpEntity(tagId = 1000)
        class CarModel {
            @DaxpField(tagId = 1002) String brand;
            @DaxpField(tagId = 1003) String model;
            @DaxpField("FIX:1004") Integer releaseYear;

            public CarModel(String brand, String model) {
                this.brand = brand;
                this.model = model;
            }

            public CarModel(String brand, String model, Integer releaseYear) {
                this.brand = brand;
                this.model = model;
                this.releaseYear = releaseYear;
            }
        }

        @DaxpEntity(tagId = 1010)
        class IdToCarMap {
            @DaxpField(tagId = 1011) final Map<Integer, CarModel> carMap = new HashMap<>();
            @DaxpField(tagId = 1012) String description;

            public IdToCarMap() {
                this.carMap.put(1, new CarModel("Volvo", "XC90"));
                this.carMap.put(2, new CarModel("Alfa Romeo", "Giulia", 2022));
                this.carMap.put(3, new CarModel("Jaguar", "F-Type"));
                this.carMap.put(4, new CarModel("Aston Martin", "DBS", 2023));
                this.description = "Numbered Supercars Registry";
            }
        }

        daxEngine.register(CarModel.class);
        daxEngine.register(IdToCarMap.class);

        printSemanticRegister();

        IdToCarMap mapObj = new IdToCarMap();
        DaxMessage msg = msgFactory.toDaxMessage("BULK.MAP", mapObj);
        DaxPreamble preamble = new DaxPreamble();

        System.out.println(messageCodec.encode(msg, preamble));
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));

        DaxTag collectionTag = DaxTag.of(config.getAppNamespaceId(), 1011);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(collectionTag));
        System.out.println("----------------------------------------");
    }

    @Test
    @DisplayName("Verify Map collection with entity keys and entity values")
    void checkMapEntKeyEntityCollection() {
        System.out.println("------------------------------------");
        System.out.println("checkMapEntKeyEntityCollection");
        System.out.println("------------------------------------");

        @DaxpEntity(tagId = 900)
        class VinKey {
            @DaxpField(tagId = 902) String vinNumber;
            @DaxpField(tagId = 903) String countryCode;
            @DaxpField("FIX:904") Integer batchId;

            public VinKey(String vinNumber, String countryCode) {
                this.vinNumber = vinNumber;
                this.countryCode = countryCode;
            }

            public VinKey(String vinNumber, String countryCode, Integer batchId) {
                this.vinNumber = vinNumber;
                this.countryCode = countryCode;
                this.batchId = batchId;
            }
        }

        @DaxpEntity(tagId = 1000)
        class CarModel {
            @DaxpField(tagId = 1002) String brand;
            @DaxpField(tagId = 1003) String model;
            @DaxpField("FIX:1004") Integer horsepower;

            public CarModel(String brand, String model) {
                this.brand = brand;
                this.model = model;
            }

            public CarModel(String brand, String model, Integer horsepower) {
                this.brand = brand;
                this.model = model;
                this.horsepower = horsepower;
            }
        }

        @DaxpEntity(tagId = 1010)
        class VinToCarRegistry {
            @DaxpField(tagId = 1011) final Map<VinKey, CarModel> registryMap = new HashMap<>();
            @DaxpField(tagId = 1012) String registryTitle;

            public VinToCarRegistry() {
                this.registryMap.put(
                        new VinKey("VIN100283", "US"),
                        new CarModel("Tesla", "Model S")
                );
                this.registryMap.put(
                        new VinKey("VIN200481", "DE", 101),
                        new CarModel("Porsche", "Taycan", 616)
                );
                this.registryMap.put(
                        new VinKey("VIN300912", "JP"),
                        new CarModel("Nissan", "GT-R")
                );
                this.registryMap.put(
                        new VinKey("VIN400551", "IT", 202),
                        new CarModel("Ferrari", "SF90", 986)
                );
                this.registryTitle = "Global VIN-Car Registry";
            }
        }

        daxEngine.register(VinKey.class);
        daxEngine.register(CarModel.class);
        daxEngine.register(VinToCarRegistry.class);

        printSemanticRegister();

        VinToCarRegistry registry = new VinToCarRegistry();
        DaxMessage msg = msgFactory.toDaxMessage("BULK.VIN_MAP", registry);
        DaxPreamble preamble = new DaxPreamble();

        System.out.println(messageCodec.encode(msg, preamble));
        System.out.println(DaxMessageDecorator.decorate(messageCodec.encode(msg, preamble)));

        DaxTag collectionTag = DaxTag.of(config.getAppNamespaceId(), 1011);
        Assertions.assertEquals(DaxDataType.COLLECTION, semanticInspector.getDataType(collectionTag));
        System.out.println("----------------------------------------");
    }
}