package parser;

import model.Car;

import java.util.Optional;

public class CarParser {

    public record LineParseResult(Optional<Car> car, Optional<String> errorMessage) {
    }

    public Optional<Car> parse(String line) {
        return parseLine(line).car();
    }

    public LineParseResult parseLine(String line) {
        if (line.trim().isEmpty()) {
            return new LineParseResult(Optional.empty(), Optional.of("пустая строка"));
        }
        String[] parts = line.split(";", -1);
        if (parts.length != 3) {
            return new LineParseResult(
                    Optional.empty(),
                    Optional.of("ожидается формат: модель;мощность;год (ровно 3 поля через \";\")"));
        }
        try {
            Car car = new Car.CarBuilder(parts[0].trim())
                    .powerHp(Integer.parseInt(parts[1].trim()))
                    .productionYear(Integer.parseInt(parts[2].trim()))
                    .build();
            return new LineParseResult(Optional.of(car), Optional.empty());
        } catch (IllegalArgumentException e) {
            return new LineParseResult(Optional.empty(), Optional.of(e.getMessage()));
        }
    }

    public StringBuilder ToText(Car car) {
        return new StringBuilder(car.getModel()).append(";")
                .append(car.getPowerHp()).append(";")
                .append(car.getProductionYear()).append("\n");
    }
}
