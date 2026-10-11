package utility;

import model.Car;

public class CarParserText {
    public static Car getCarFromText(String str) {
        String[] split = str.split(";");
        if (split.length != 3) {
            throw new RuntimeException("Not possible create correct car from this text: " + str);
        }
        return new Car.CarBuilder(split[0])
                .powerHp(Integer.parseInt(split[1]))
                .productionYear(Integer.parseInt(split[2]))
                .build();
    }

    public static StringBuilder carToText(Car car) {
        return new StringBuilder(car.getModel()).append(";")
                .append(car.getPowerHp()).append(";")
                .append(car.getProductionYear()).append("\n");

    }
}
