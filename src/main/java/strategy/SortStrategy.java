package strategy;

import collection.CarArray;
import comparator.CarComparator;

public interface SortStrategy {
    CarArray sort(CarArray array, CarComparator comparator);
}
