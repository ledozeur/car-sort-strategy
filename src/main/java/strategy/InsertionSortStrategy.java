package strategy;

import collection.CarArray;
import comparator.CarComparator;
import model.Car;

public class InsertionSortStrategy implements SortStrategy {
    @Override
    public CarArray sort(CarArray array, CarComparator comparator) {
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
}
