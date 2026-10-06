package comparator;

import main.model.Car;

public class CarPowerHpComparator implements CarComparator {
    @Override
    public int compare(Car a, Car b) {
        return Integer.compare(a.getPowerHp(), b.getPowerHp());
    }
}
