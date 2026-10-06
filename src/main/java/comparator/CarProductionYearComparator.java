package comparator;

import model.Car;

public class CarProductionYearComparator implements CarComparator {
    @Override
    public int compare(Car a, Car b) {
        return Integer.compare(a.getProductionYear(), b.getProductionYear());
    }
}
