package strategy;

import collection.CarArray;
import comparator.CarComparator;
import model.Car;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class EvenOddSortDecorator implements SortStrategy {
    private final SortStrategy sortStrategy;
    private final Function<Car, Integer> valueExtractor;

    public EvenOddSortDecorator(SortStrategy sortStrategy, Function<Car, Integer> valueExtractor) {
        this.sortStrategy = sortStrategy;
        this.valueExtractor = valueExtractor;
    }

    @Override
    public CarArray sort(CarArray old, CarComparator comparator) {
        CarArray array = createCopy(old);
        List<Integer> indexes = new ArrayList<>();
        CarArray sortableElements = new CarArray();
        for (int i = 0; i < array.size(); i++) {
            Car car = array.get(i);

            if (valueExtractor.apply(car) % 2 == 0) {
                indexes.add(i);
                sortableElements.add(car);
            }
        }
        sortStrategy.sort(sortableElements, comparator);

        for (int i = 0; i < indexes.size(); i++) {
            array.set(indexes.get(i), sortableElements.get(i));
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
