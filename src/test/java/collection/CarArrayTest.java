package collection;

import model.Car;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarArrayTest {

    private static Car car(String model) {
        return new Car.CarBuilder(model).build();
    }

    private static void assertIndexRejected(Executable call) {
        Exception e = assertThrows(IndexOutOfBoundsException.class, call);
        assertEquals(IndexOutOfBoundsException.class, e.getClass());
    }

    // --- add/size ---

    @Test
    void newCollectionIsEmpty() {
        assertEquals(0, new CarArray().size());
    }

    @Test
    void addStoresElementsInOrder() {
        CarArray cars = new CarArray();
        Car a = car("A");
        Car b = car("B");

        cars.add(a);
        cars.add(b);

        assertEquals(2, cars.size());
        assertSame(a, cars.get(0));
        assertSame(b, cars.get(1));
    }

    @Test
    void addReturnsThisForChaining() {
        CarArray cars = new CarArray();

        assertSame(cars, cars.add(car("A")));
        assertEquals(2, cars.add(car("B")).size());
    }

    @Test
    void addNullThrowsNpeAndKeepsSize() {
        CarArray cars = new CarArray().add(car("A"));

        assertThrows(NullPointerException.class, () -> cars.add(null));
        assertEquals(1, cars.size());
    }

    // --- capacity growth ---

    @Test
    void growsBeyondDefaultCapacity() {
        CarArray cars = new CarArray();
        Car[] added = new Car[40];

        for (int i = 0; i < added.length; i++) {
            added[i] = car("Model" + i);
            cars.add(added[i]);
        }

        assertEquals(40, cars.size());
        for (int i = 0; i < added.length; i++) {
            assertSame(added[i], cars.get(i));
        }
    }

    @Test
    void growsExactlyAtCapacityBoundary() {
        CarArray cars = new CarArray();
        for (int i = 0; i < 32; i++) {
            cars.add(car("Model" + i));
        }
        Car nextOne = car("Last");

        cars.add(nextOne);

        assertEquals(33, cars.size());
        assertSame(nextOne, cars.get(32));
        assertEquals("Model0", cars.get(0).getModel());
    }

    // --- get ---

    @Test
    void getRejectsOutOfRangeIndex() {
        CarArray cars = new CarArray().add(car("A")).add(car("B")).add(car("C"));

        assertIndexRejected(() -> cars.get(-1));
        assertIndexRejected(() -> cars.get(3));
        assertIndexRejected(() -> cars.get(10));
    }

    @Test
    void getOnEmptyCollectionThrows() {
        assertIndexRejected(() -> new CarArray().get(0));
    }

    // --- set ---

    @Test
    void setReplacesElementAndKeepsSize() {
        CarArray cars = new CarArray().add(car("A")).add(car("B"));
        Car replacement = car("New");

        CarArray result = cars.set(1, replacement);

        assertSame(cars, result);
        assertSame(replacement, cars.get(1));
        assertEquals("A", cars.get(0).getModel());
        assertEquals(2, cars.size());
    }

    @Test
    void setNullThrowsNpeAndKeepsElement() {
        Car original = car("A");
        CarArray cars = new CarArray().add(original);

        assertThrows(NullPointerException.class, () -> cars.set(0, null));
        assertSame(original, cars.get(0));
    }

    @Test
    void setRejectsOutOfRangeIndex() {
        CarArray cars = new CarArray().add(car("A"));

        assertIndexRejected(() -> cars.set(-1, car("X")));
        assertIndexRejected(() -> cars.set(1, car("X")));
        assertEquals(1, cars.size());
    }

    // --- clear ---

    @Test
    void clearEmptiesCollection() {
        CarArray cars = new CarArray().add(car("A")).add(car("B"));

        cars.clear();

        assertEquals(0, cars.size());
        assertIndexRejected(() -> cars.get(0));
        assertFalse(cars.iterator().hasNext());
    }

    @Test
    void collectionIsUsableAfterClear() {
        CarArray cars = new CarArray();
        for (int i = 0; i < 40; i++) {
            cars.add(car("Model" + i));
        }

        cars.clear();
        Car fresh = car("Fresh");
        cars.add(fresh);

        assertEquals(1, cars.size());
        assertSame(fresh, cars.get(0));
    }

    // --- toArray ---

    @Test
    void toArrayLengthEqualsSizeNotCapacity() {
        CarArray cars = new CarArray().add(car("A")).add(car("B")).add(car("C"));

        Car[] array = cars.toArray();

        assertEquals(3, array.length);
        for (int i = 0; i < array.length; i++) {
            assertSame(cars.get(i), array[i]);
        }
    }

    @Test
    void toArrayOfEmptyCollectionIsEmpty() {
        assertEquals(0, new CarArray().toArray().length);
    }

    @Test
    void toArrayReturnsIndependentCopy() {
        Car original = car("A");
        CarArray cars = new CarArray().add(original);

        Car[] array = cars.toArray();
        array[0] = car("Other");

        assertSame(original, cars.get(0));
    }

    // --- iterator ---

    @Test
    void iteratorOnEmptyCollectionHasNothing() {
        int visited = 0;
        for (Car ignored : new CarArray()) {
            visited++;
        }

        assertEquals(0, visited);
    }

    @Test
    void iteratorVisitsAllElementsInOrder() {
        CarArray cars = new CarArray();
        for (int i = 0; i < 40; i++) {
            cars.add(car("Model" + i));
        }

        int index = 0;
        for (Car c : cars) {
            assertSame(cars.get(index), c);
            index++;
        }

        assertEquals(40, index);
    }

    @Test
    void iteratorsAreIndependent() {
        CarArray cars = new CarArray().add(car("A")).add(car("B")).add(car("C"));

        int pairs = 0;
        for (Car ignoredOuter : cars) {
            for (Car ignoredInner : cars) {
                pairs++;
            }
        }

        assertEquals(9, pairs);
    }

    @Test
    void nextOnExhaustedIteratorThrows() {
        CarArray cars = new CarArray().add(car("A"));
        Iterator<Car> it = cars.iterator();

        it.next();

        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void hasNextIsTrueUntilLastElement() {
        Iterator<Car> it = new CarArray().add(car("A")).add(car("B")).iterator();

        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
        assertFalse(it.hasNext());
    }
}