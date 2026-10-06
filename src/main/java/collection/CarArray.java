package collection;

import model.Car;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class CarArray implements Iterable<Car> {
    private static final int DEFAULT_CAPACITY = 32;
    private int size;
    private Car[] data;

    public CarArray() {
        data = new Car[DEFAULT_CAPACITY];
    }

    public CarArray add(Car car) {
        Objects.requireNonNull(car, "Trying to add null pointer car");
        if (size == data.length) {
            int newCapacity = data.length * 2;
            Car[] newData = new Car[newCapacity];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
        data[size] = car;
        size++;
        return this;
    }

    public int size() {
        return size;
    }

    public Car get(int index) {
        checkIndex(index);
        return data[index];
    }

    public CarArray set(int index, Car car) {
        Objects.requireNonNull(car, "Trying to set null pointer car");
        checkIndex(index);
        data[index] = car;
        return this;
    }

    public void clear() {
        data = new Car[DEFAULT_CAPACITY];
        size = 0;
    }

    public Car[] toArray() {
        Car[] newData = new Car[size];
        System.arraycopy(data, 0, newData, 0, size);
        return newData;
    }

    @Override
    public Iterator<Car> iterator() {
        return new CarArrayIterator();
    }

    private class CarArrayIterator implements Iterator<Car> {
        private int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public Car next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Has not next, size: " + size);
            }
            return data[cursor++];
        }
    }

    private void checkIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }
}