package strategy;

import collection.CarArray;
import comparator.CarComparator;
import model.Car;

import java.util.stream.IntStream;

public class InsertionSortStrategy implements SortStrategy {
    @Override
    public CarArray sort(CarArray old, CarComparator comparator) {
        CarArray array = createCopy(old);
        for (int i = 1; i < array.size(); i++) {
            Car current = array.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(array.get(j), current) > 0) {
                array.set(j + 1, array.get(j));
                j -= 1;
            }
            array.set(j + 1, current);
        }
        return array;
    }

    private CarArray createCopy(CarArray old) {
        CarArray copy = new CarArray();
        IntStream.range(0, old.size())
                .mapToObj(old::get)
                .forEach(copy::add);
        return copy;
    }
}
