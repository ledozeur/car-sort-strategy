package strategy;

import collection.CarArray;
import comparator.CarComparator;
import model.Car;

public class BubbleSortStrategy implements SortStrategy {

    @Override
    public CarArray sort(CarArray array, CarComparator comparator) {
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
}
