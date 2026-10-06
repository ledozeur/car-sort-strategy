package comparator;

import main.model.Car;

public class CarModelComparator implements CarComparator {
    @Override
    public int compare(Car a, Car b) {
        return a.getModel().compareTo(b.getModel());
    }
}
