package concurrency;

import collection.CarArray;
import model.Car;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParallelCountServiceTest {

    private final ParallelCountService service = new ParallelCountService();

    private static Car car(String model, int powerHp, int year) {
        return new Car.CarBuilder(model)
                .powerHp(powerHp)
                .productionYear(year)
                .build();
    }

    private static Car target() {
        return car("Camry", 249, 2021);
    }

    // array of `size` cars where every `step`-th element (starting from 0) equals target
    private static CarArray arrayWithEvery(int step, int size) {
        CarArray array = new CarArray();
        for (int i = 0; i < size; i++) {
            array.add(i % step == 0 ? target() : car("Other" + i, 100, 2000));
        }
        return array;
    }

    // --- basic counting ---

    @Test
    void countsOccurrencesWithSingleThread() {
        CarArray array = new CarArray()
                .add(target())
                .add(car("Corolla", 139, 2019))
                .add(target())
                .add(car("Civic", 158, 2018));

        assertEquals(2, service.count(array, target(), 1));
    }

    @Test
    void returnsZeroWhenTargetIsAbsent() {
        CarArray array = new CarArray()
                .add(car("Corolla", 139, 2019))
                .add(car("Civic", 158, 2018));

        assertEquals(0, service.count(array, target(), 4));
    }

    @Test
    void emptyArrayGivesZero() {
        assertEquals(0, service.count(new CarArray(), target(), 4));
    }

    // --- equals, not identity ---

    @Test
    void countsEqualCarsThatAreDifferentObjects() {
        CarArray array = new CarArray()
                .add(car("Camry", 249, 2021))
                .add(car("Camry", 249, 2021));

        assertEquals(2, service.count(array, car("Camry", 249, 2021), 2));
    }

    @Test
    void doesNotCountCarsDifferingInAnyField() {
        CarArray array = new CarArray()
                .add(car("Camry", 249, 2021))
                .add(car("Camry", 250, 2021))
                .add(car("Camry", 249, 2022))
                .add(car("Corolla", 249, 2021));

        assertEquals(1, service.count(array, car("Camry", 249, 2021), 2));
    }

    // --- range splitting ---

    @Test
    void sameResultForAnyThreadCount() {
        CarArray array = arrayWithEvery(7, 100);

        // up to 150 threads: includes cases with more threads than elements
        for (int threads = 1; threads <= 150; threads++) {
            assertEquals(15, service.count(array, target(), threads), "threadCount=" + threads);
        }
    }

    @Test
    void noElementIsLostWhenSizeIsNotDivisibleByThreadCount() {
        // every element matches, so any lost or doubled element changes the answer
        CarArray array = arrayWithEvery(1, 10);

        for (int threads = 1; threads <= 15; threads++) {
            assertEquals(10, service.count(array, target(), threads), "threadCount=" + threads);
        }
    }

    @Test
    void findsTargetOnlyAtFirstPosition() {
        CarArray array = new CarArray().add(target());
        for (int i = 1; i < 10; i++) {
            array.add(car("Other" + i, 100, 2000));
        }

        for (int threads = 1; threads <= 12; threads++) {
            assertEquals(1, service.count(array, target(), threads), "threadCount=" + threads);
        }
    }

    @Test
    void findsTargetOnlyAtLastPosition() {
        CarArray array = new CarArray();
        for (int i = 0; i < 9; i++) {
            array.add(car("Other" + i, 100, 2000));
        }
        array.add(target());

        // the last element is the one that disappears with naive size / threadCount splitting
        for (int threads = 1; threads <= 12; threads++) {
            assertEquals(1, service.count(array, target(), threads), "threadCount=" + threads);
        }
    }

    @Test
    void moreThreadsThanElements() {
        CarArray array = new CarArray().add(target()).add(target()).add(car("Other", 100, 2000));

        assertEquals(2, service.count(array, target(), 10));
    }

    // --- repeated runs ---

    @Test
    void resultIsStableAcrossManyRuns() {
        // indexes 0, 3, ..., 19998 -> 6667 matches
        CarArray array = arrayWithEvery(3, 20_000);

        for (int run = 0; run < 300; run++) {
            assertEquals(6667, service.count(array, target(), 8), "run=" + run);
        }
    }

    // --- side effects ---

    @Test
    void doesNotModifyArray() {
        Car first = target();
        Car second = car("Corolla", 139, 2019);
        CarArray array = new CarArray().add(first).add(second);

        service.count(array, target(), 3);

        assertEquals(2, array.size());
        assertSame(first, array.get(0));
        assertSame(second, array.get(1));
    }

    // --- invalid arguments ---

    @Test
    void rejectsNonPositiveThreadCount() {
        CarArray array = new CarArray().add(target());

        assertThrows(IllegalArgumentException.class, () -> service.count(array, target(), 0));
        assertThrows(IllegalArgumentException.class, () -> service.count(array, target(), -1));
    }

    @Test
    void rejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> service.count(null, target(), 2));
        assertThrows(NullPointerException.class, () -> service.count(new CarArray(), null, 2));
    }

    // --- console output ---

    @Test
    void countAndPrintWritesResultToConsole() {
        CarArray array = new CarArray().add(target()).add(target()).add(car("Corolla", 139, 2019));

        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            service.countAndPrint(array, target(), 2);
        } finally {
            System.setOut(original);
        }

        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains(target().toString()), "Output must mention the target car: " + output);
        assertTrue(output.contains(": 2"), "Output must contain the result: " + output);
    }
}