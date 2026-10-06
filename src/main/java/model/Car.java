package model;

import validation.CarValidator;

import java.util.Objects;

public final class Car {
    private final String model;
    private final int powerHp;
    private final int productionYear;

    private Car(CarBuilder cb) {
        this.model = cb.model;
        this.powerHp = cb.powerHp;
        this.productionYear = cb.productionYear;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return powerHp == car.powerHp && productionYear == car.productionYear && Objects.equals(model, car.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model, powerHp, productionYear);
    }

    @Override
    public String toString() {
        return "Car{" +
                "model='" + model + '\'' +
                ", powerHp=" + powerHp +
                ", productionYear=" + productionYear +
                '}';
    }

    public static class CarBuilder {
        private String model;
        private int powerHp = 100;
        private int productionYear = 2000;

        public CarBuilder(String model) {
            if(Objects.isNull(model)) {
                throw new NullPointerException("Car model must not be null");
            }
            this.model = CarValidator.modelValidation(model);
        }

        public CarBuilder powerHp(int powerHp) {
            this.powerHp = CarValidator.powerHpValidation(powerHp);
            return this;
        }

        public CarBuilder productionYear(int productionYear) {
            this.productionYear = CarValidator.productionYearValidation(productionYear);
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }

    public String getModel() {
        return model;
    }

    public int getPowerHp() {
        return powerHp;
    }

    public int getProductionYear() {
        return productionYear;
    }
}
