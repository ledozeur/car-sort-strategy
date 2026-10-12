package fill;

import collection.CarArray;
import model.Car;
import parser.CarParser;

import java.util.Optional;
import java.util.Scanner;

public class ManualCarFillService implements CarFillService {
    private final Scanner scanner;
    private final CarParser parser = new CarParser();

    public ManualCarFillService(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public CarArray create(Integer limit) {
        CarArray array = new CarArray();
        boolean scan = true;
        while (scan) {
            System.out.println("\n\n\n");
            if (array.size() > 0) {
                System.out.println(array);
                System.out.println("Для сохранения данных и выхода введите \"-1\"");
                System.out.println("Для сброса списка введите \"-2\"");
            }
            if (array.size() >= limit && limit != -1) {
                return array;
            }

            String text = scanner.nextLine();

            if (text.trim().equals("-1")) {
                scan = false;
                continue;
            }
            if (text.trim().equals("-2")) {
                array.clear();
                continue;
            }

            Optional<Car> car = parser.parse(text);
            if (car.isPresent()) {
                array.add(car.get());
            } else {
                System.out.println("Введенный текст не может быть конвертирован в car");
            }


        }
        return array;
    }


}
