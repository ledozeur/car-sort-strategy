package comparator;

import model.Car;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarComparatorTest {

    private final CarComparator byModel = new CarModelComparator();
    private final CarComparator byPowerHp = new CarPowerHpComparator();
    private final CarComparator byYear = new CarProductionYearComparator();

    private static Car car(String model, int powerHp, int year) {
        return new Car.CarBuilder(model)
                .powerHp(powerHp)
                .productionYear(year)
                .build();
    }

    // --- by model ---

    @Test
    void modelComparatorOrdersAlphabetically() {
        Car audi = car("Audi", 150, 2020);
        Car bmw = car("BMW", 150, 2020);

        assertTrue(byModel.compare(audi, bmw) < 0);
        assertTrue(byModel.compare(bmw, audi) > 0);
    }

    @Test
    void modelComparatorReturnsZeroForSameModelAndIgnoresOtherFields() {
        Car a = car("Camry", 100, 2000);
        Car b = car("Camry", 900, 2020);

        assertEquals(0, byModel.compare(a, b));
    }

    @Test
    void modelComparatorDistinguishesCaseOfSameLetters() {
        Car upper = car("Audi", 150, 2020);
        Car lower = car("audi", 150, 2020);

        assertTrue(byModel.compare(upper, lower) < 0);
        assertTrue(byModel.compare(lower, upper) > 0);
    }

    @Test
    void modelComparatorPlacesUppercaseBeforeLowercaseRegardlessOfLetter() {
        Car upper = car("BMW", 150, 2020);
        Car lower = car("audi", 150, 2020);

        // 'B'(66) must be lower than 'a'(97)
        assertTrue(byModel.compare(upper, lower) < 0);
    }

    // --- by powerhp ---

    @Test
    void powerHpComparatorOrdersByPower() {
        Car weak = car("A", 90, 2020);
        Car strong = car("B", 400, 2020);

        assertTrue(byPowerHp.compare(weak, strong) < 0);
        assertTrue(byPowerHp.compare(strong, weak) > 0);
    }

    @Test
    void powerHpComparatorReturnsZeroForSamePowerAndIgnoresOtherFields() {
        Car a = car("A", 200, 2001);
        Car b = car("Z", 200, 2022);

        assertEquals(0, byPowerHp.compare(a, b));
    }

    // --- by productionyear ---

    @Test
    void yearComparatorOrdersByYear() {
        Car old = car("A", 150, 1999);
        Car recent = car("B", 150, 2021);

        assertTrue(byYear.compare(old, recent) < 0);
        assertTrue(byYear.compare(recent, old) > 0);
    }

    @Test
    void yearComparatorReturnsZeroForSameYearAndIgnoresOtherFields() {
        Car a = car("A", 90, 2015);
        Car b = car("Z", 700, 2015);

        assertEquals(0, byYear.compare(a, b));
    }
}