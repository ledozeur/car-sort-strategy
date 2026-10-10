package sort;

import collection.CarArray;
import comparator.CarPowerHpComparator;
import model.Car;
import org.junit.jupiter.api.Test;
import strategy.EvenOddSortDecorator;
import strategy.InsertionSortStrategy;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvenOddSortTest {
    private final EvenOddSortDecorator decorator =
            new EvenOddSortDecorator(
                    new InsertionSortStrategy(),
                    Car::getPowerHp
            );

    @Test
    void shouldSortCarsWithEvenPower() {
        CarArray cars = new CarArray();
        cars.add(car("Odd-101", 101));
        cars.add(car("Even-300", 300));
        cars.add(car("Even-100", 100));
        cars.add(car("Odd-201", 201));
        cars.add(car("Even-200", 200));

        decorator.sort(cars, new CarPowerHpComparator());

        assertEquals(
                List.of(101, 100, 200, 201, 300),
                getPowerValues(cars)
        );
    }

    @Test
    void shouldKeepCarsWithOddPowerAtOriginalPositions() {
        CarArray cars = new CarArray();
        cars.add(car("Odd-101", 101));
        cars.add(car("Even-300", 300));
        cars.add(car("Odd-201", 201));
        cars.add(car("Even-100", 100));
        cars.add(car("Odd-301", 301));
        cars.add(car("Even-200", 200));

        decorator.sort(cars, new CarPowerHpComparator());

        assertEquals(101, cars.get(0).getPowerHp());
        assertEquals(201, cars.get(2).getPowerHp());
        assertEquals(301, cars.get(4).getPowerHp());
    }

    private Car car(String name, int powerHp) {
        return new Car.CarBuilder(name)
                .powerHp(powerHp)
                .productionYear(2020)
                .build();
    }

    private List<Integer> getPowerValues(CarArray cars) {
        return IntStream.range(0, cars.size())
                .mapToObj(i -> cars.get(i).getPowerHp())
                .toList();
    }
}
