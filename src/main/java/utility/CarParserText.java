package utility;

import collection.CarArray;
import model.Car;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public static void printArray(CarArray array) {
        String text = IntStream.range(0, array.size())
                .mapToObj(array::get)
                .map(CarParserText::carToText)
                .collect(Collectors.joining())
                .trim();
        System.out.println(text);
    }
}
