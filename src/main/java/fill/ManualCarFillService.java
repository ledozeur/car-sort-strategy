package fill;

import collection.CarArray;
import model.Car;
import parser.CarParser;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class ManualCarFillService implements CarFillService {
    private final Scanner scanner;
    private final CarParser parser = new CarParser();

    public ManualCarFillService(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public CarArray create(Integer limit) {
        StringBuilder cars = new StringBuilder();
        boolean scan = true;
        while (scan) {
            System.out.println("\n\n\n");
            if (!cars.isEmpty()) {
                System.out.println(cars);
                System.out.println("Для сохранения данных и выхода введите \"-1\"");
                System.out.println("Для сброса списка введите \"-2\"");
            }
            if (getCarCount(cars) >= limit && limit != -1) {
                CarArray array = getArray(cars);
                if (array.size() >= limit) {
                    return array;
                }
            }

            String text = scanner.nextLine();

            if (text.trim().equals("-1")) {
                scan = false;
                continue;
            }
            if (text.trim().equals("-2")) {
                cars = new StringBuilder();
                continue;
            }

            cars.append(text).append("\n");

        }
        return getArray(cars);
    }

    private long getCarCount(StringBuilder cars) {
        return cars.chars()
                .filter(c -> c == '\n')
                .count();
    }

    private CarArray getArray(StringBuilder cars) {
        CarArray array = new CarArray();
        String[] lines = cars.toString().split("\n");

        IntStream.range(0, lines.length)
                .forEach(i -> {
                    Optional<Car> car = parser.parse(lines[i]);

                    car.ifPresentOrElse(
                            array::add,
                            () -> System.out.println(
                                    "Невалидная строка № " + (i + 1)
                            )
                    );
                });

        return array;
    }

}
