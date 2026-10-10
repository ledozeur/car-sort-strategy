package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarTest {

    // --- Builder ---

    @Test
    void builderUsesDefaultsWhenOnlyModelGiven() {
        Car car = new Car.CarBuilder("Camry").build();

        assertEquals("Camry", car.getModel());
        assertEquals(100, car.getPowerHp());
        assertEquals(2000, car.getProductionYear());
    }

    @Test
    void builderSetsAllFields() {
        Car car = new Car.CarBuilder("Camry")
                .powerHp(249)
                .productionYear(2021)
                .build();

        assertEquals("Camry", car.getModel());
        assertEquals(249, car.getPowerHp());
        assertEquals(2021, car.getProductionYear());
    }

    @Test
    void nullModelThrowsNpe() {
        assertThrows(NullPointerException.class, () -> new Car.CarBuilder(null));
    }

    @Test
    void builderRejectsInvalidModel() {
        assertThrows(IllegalArgumentException.class, () -> new Car.CarBuilder(""));
    }

    @Test
    void builderRejectsInvalidPowerHp() {
        assertThrows(IllegalArgumentException.class,
                () -> new Car.CarBuilder("X").powerHp(-1));
    }

    @Test
    void builderRejectsInvalidProductionYear() {
        assertThrows(IllegalArgumentException.class,
                () -> new Car.CarBuilder("X").productionYear(1800));
    }

    // --- equals/hashCode ---

    @Test
    void equalCarsAreEqualAndHaveSameHashCode() {
        Car a = new Car.CarBuilder("Camry").powerHp(200).productionYear(2020).build();
        Car b = new Car.CarBuilder("Camry").powerHp(200).productionYear(2020).build();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void carsDifferingInAnyFieldAreNotEqual() {
        Car base = new Car.CarBuilder("Camry").powerHp(200).productionYear(2020).build();

        assertNotEquals(base, new Car.CarBuilder("Corolla").powerHp(200).productionYear(2020).build());
        assertNotEquals(base, new Car.CarBuilder("Camry").powerHp(201).productionYear(2020).build());
        assertNotEquals(base, new Car.CarBuilder("Camry").powerHp(200).productionYear(2021).build());
    }

    @Test
    void carIsEqualToItself() {
        Car car = new Car.CarBuilder("Camry").build();
        assertEquals(car, car);
    }

    @Test
    void carIsNotEqualToNullOrOtherType() {
        Car car = new Car.CarBuilder("Camry").build();
        assertFalse(car.equals(null));
        assertFalse(car.equals("Camry"));
    }

    // --- toString ---

    @Test
    void toStringContainsAllFields() {
        String s = new Car.CarBuilder("Camry").powerHp(200).productionYear(2020).build().toString();

        assertTrue(s.contains("Camry"));
        assertTrue(s.contains("200"));
        assertTrue(s.contains("2020"));
    }
}