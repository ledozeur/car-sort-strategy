package strategy;

import collection.CarArray;
import comparator.CarComparator;
import model.Car;

import java.util.stream.IntStream;

public class BubbleSortStrategy implements SortStrategy {

    @Override
    public CarArray sort(CarArray old, CarComparator comparator) {
        CarArray array = createCopy(old);
        for (int i = 0; i < array.size(); i++) {
            for (int j = 0; j < array.size() - i - 1; j++) {
                if (comparator.compare(array.get(j), array.get(j + 1)) > 0) {
                    Car temp = array.get(j);
                    array.set(j, array.get(j + 1));
                    array.set(j + 1, temp);
                }
            }
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
