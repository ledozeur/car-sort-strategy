package comparator;

import model.Car;

public class CarNaturalOrderComparator implements CarComparator {
    private final CarComparator byModel = new CarModelComparator();
    private final CarComparator byPowerHp = new CarPowerHpComparator();
    private final CarComparator byYear = new CarProductionYearComparator();

    @Override
    public int compare(Car a, Car b) {
        int result = byModel.compare(a, b);
        if (result != 0) {
            return result;
        }
        result = byPowerHp.compare(a, b);
        if (result != 0) {
            return result;
        }
        return byYear.compare(a, b);
    }
}