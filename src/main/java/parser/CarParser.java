package parser;

import model.Car;

import java.util.Optional;

public class CarParser {
    public Optional<Car> parse(String line) {
        String[] parts = line.split(";", -1);
        if (parts.length != 3) {
            return Optional.empty();
        }
        try {
            return Optional.of(new Car.CarBuilder(parts[0].trim())
                    .powerHp(Integer.parseInt(parts[1].trim()))
                    .productionYear(Integer.parseInt(parts[2].trim()))
                    .build());
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public StringBuilder ToText(Car car) {
        return new StringBuilder(car.getModel()).append(";")
                .append(car.getPowerHp()).append(";")
                .append(car.getProductionYear()).append("\n");
    }
}
