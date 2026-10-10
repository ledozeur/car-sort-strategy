package sort;

import collection.CarArray;
import comparator.CarModelComparator;
import comparator.CarPowerHpComparator;
import comparator.CarProductionYearComparator;
import fill.RandomCarFillService;
import org.junit.jupiter.api.Test;
import strategy.InsertionSortStrategy;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class InsertionSortTest {
    private final RandomCarFillService fillService = new RandomCarFillService();
    private final InsertionSortStrategy sortStrategy = new InsertionSortStrategy();

    @Test
    void hpSortTest() {
        boolean isSorted = true;
        CarArray array = fillService.create(10);
        sortStrategy.sort(array, new CarPowerHpComparator());
        for (int i = 1; i < array.size(); i++) {
            if (array.get(i).getPowerHp() < array.get(i - 1).getPowerHp()) {
                isSorted = false;
            }
        }
        assertTrue(isSorted);
    }

    @Test
    void yearSortTest() {
        boolean isSorted = true;
        CarArray array = fillService.create(10);
        sortStrategy.sort(array, new CarProductionYearComparator());
        for (int i = 1; i < array.size(); i++) {
            if (array.get(i).getProductionYear() < array.get(i - 1).getProductionYear()) {
                isSorted = false;
            }
        }
        assertTrue(isSorted);
    }

    @Test
    void modelSortTest() {
        boolean isSorted = true;
        CarArray array = fillService.create(10);
        sortStrategy.sort(array, new CarModelComparator());
        for (int i = 1; i < array.size(); i++) {
            if (new CarModelComparator().compare(array.get(i), array.get(i - 1)) < 0) {
                isSorted = false;
            }
        }
        assertTrue(isSorted);
    }
}
