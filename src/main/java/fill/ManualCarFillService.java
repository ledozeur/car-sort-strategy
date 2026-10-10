package fill;

import collection.CarArray;
import utility.CarParserText;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static utility.CarParserText.getCarFromText;

public class ManualCarFillService implements CarFillService {
    private final Scanner scanner;

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
                printArray(array);
                System.out.println("Для сохранения данных и выхода введите \"-1\"");
            }
            if (array.size() >= limit && limit != -1) {
                return array;
            }

            String text = scanner.nextLine();

            if (text.trim().equals("-1")) {
                scan = false;
            }
            try {
                array.add(getCarFromText(text));
            } catch (RuntimeException e) {
                System.out.println(e);
            }
        }
        return array;
    }

    private void printArray(CarArray array) {
        String text = IntStream.range(0, array.size())
                .mapToObj(array::get)
                .map(CarParserText::carToText)
                .collect(Collectors.joining())
                .trim();
        System.out.println(text);
    }
}
