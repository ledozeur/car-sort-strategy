package menu;

import collection.CarArray;
import fill.FileCarFillService;
import fill.ManualCarFillService;
import fill.RandomCarFillService;
import parser.CarParser;
import utility.CarParserText;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Scanner;

public class MenuController {

    private CarArray cars = new CarArray();
    private CarArray lastSorted;

    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "1 — заполнить массив\n" +
                            "2 — показать\n" +
                            "3 — сортировать\n" +
                            "4 — записать в файл\n" +
                            "5 — подсчитать N\n" +
                            "0 — выход");

            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Введите число от 0 до 5.");
                continue;
            }
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: {
                    boolean exitFill = false;
                    while (!exitFill) {
                        System.out.println(
                                "\nЗаполнение:\n" +
                                        "1 — из файла\n" +
                                        "2 — случайно\n" +
                                        "3 — вручную\n" +
                                        "0 — назад");

                        if (!sc.hasNextInt()) {
                            sc.nextLine();
                            System.out.println("Введите число от 0 до 3.");
                            continue;
                        }
                        int fillChoice = sc.nextInt();
                        sc.nextLine();

                        switch (fillChoice) {
                            case 0:
                                exitFill = true;
                                break;
                            case 1: {
                                System.out.println("Путь к файлу (Enter — data/cars.txt): ");
                                String pathInput = sc.nextLine().trim();
                                String filePath;
                                if (pathInput.isEmpty()) {
                                    filePath = "data/cars.txt";
                                } else {
                                    filePath = pathInput;
                                }

                                System.out.println(
                                        "Лимит автомобилей (-1 — все из файла, иначе целое число > 0): ");
                                if (!sc.hasNextInt()) {
                                    sc.nextLine();
                                    System.out.println("Введите -1 или целое число > 0");
                                    continue;
                                }
                                int fileLimit = sc.nextInt();
                                sc.nextLine();
                                if (fileLimit != -1 && fileLimit <= 0) {
                                    System.out.println("Нужно -1 или число > 0. Попробуйте снова.");
                                    continue;
                                }

                                try {
                                    FileCarFillService fileFill =
                                            new FileCarFillService(Path.of(filePath), new CarParser());
                                    cars = fileFill.create(fileLimit);
                                    System.out.println("Загружено автомобилей: " + cars.size());
                                } catch (UncheckedIOException e) {
                                    System.out.println("Не удалось прочитать файл: " + filePath);
                                }
                                break;
                            }
                            case 2:

                                System.out.println("Введите количество автомобилей: ");
                                if (!sc.hasNextInt()) {
                                    sc.nextLine();
                                    System.out.println("Введите целое число > 0");
                                    continue;
                                }
                                int n = sc.nextInt();
                                sc.nextLine();
                                if (n <= 0) {
                                    System.out.println("Вы ввели число <= 0. Попробуйте снова: ");
                                    continue;
                                }
                                RandomCarFillService r = new RandomCarFillService();
                                cars = r.create(n);


                                break;
                            case 3: {
                                System.out.println(
                                        "Лимит автомобилей (-1 — без лимита, иначе целое число > 0): ");
                                if (!sc.hasNextInt()) {
                                    sc.nextLine();
                                    System.out.println("Введите -1 или целое число > 0");
                                    continue;
                                }
                                int limit = sc.nextInt();
                                sc.nextLine();
                                if (limit != -1 && limit <= 0) {
                                    System.out.println("Нужно -1 или число > 0. Попробуйте снова.");
                                    continue;
                                }
                                System.out.println(
                                        "Ввод в формате: модель;мощность;год\n" +
                                                "Для выхода с сохранением: -1, сброс списка: -2");
                                ManualCarFillService manual = new ManualCarFillService(sc);
                                cars = manual.create(limit);
                                break;
                            }
                            default:
                                System.out.println("Нет такого пункта. Введите число от 0 до 3.");
                                break;
                        }
                    }
                    break;
                }
                case 2:

                    if (!(cars.size() == 0)) {
                        CarParserText.printArray(cars);
                    } else {
                        System.out.println("Массив пуст.");
                    }

                    break;
                case 3: {
                    System.out.println(
                            "\nАлгоритм:\n" +
                                    "1 — Bubble Sort\n" +
                                    "2 — Insertion Sort\n" +
                                    "3 — Even/Odd\n" +
                                    "0 — назад");

                    if (!sc.hasNextInt()) {
                        sc.nextLine();
                        System.out.println("Введите число от 0 до 3.");
                        break;
                    }
                    int alg = sc.nextInt();
                    sc.nextLine();

                    switch (alg) {
                        case 0:
                            break;
                        case 1:
                            // TODO: BubbleSortStrategy
                            break;
                        case 2:
                            // TODO: InsertionSortStrategy
                            break;
                        case 3:
                            // TODO: EvenOddSortDecorator
                            break;
                        default:
                            System.out.println("Неверный алгоритм.");
                            break;
                    }
                    if (alg == 0) {
                        break;
                    }

                    System.out.println(
                            "\nПоле:\n" +
                                    "1 — модель\n" +
                                    "2 — мощность\n" +
                                    "3 — год выпуска\n" +
                                    "0 — назад");

                    if (!sc.hasNextInt()) {
                        sc.nextLine();
                        System.out.println("Введите число от 0 до 3.");
                        break;
                    }
                    int field = sc.nextInt();
                    sc.nextLine();

                    switch (field) {
                        case 0:
                            break;
                        case 1:
                            // TODO: CarModelComparator
                            break;
                        case 2:
                            // TODO: CarPowerHpComparator
                            break;
                        case 3:
                            // TODO: CarProductionYearComparator
                            break;
                        default:
                            System.out.println("Неверное поле.");
                            break;
                    }

                    // TODO: CopyCarArray + strategy.sort + lastSorted + вывод
                    break;
                }
                case 4:
                    // TODO: ResultFileWriter.save(lastSorted)
                    break;
                case 5:
                    // TODO: ParallelCountService
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Нет такого пункта. Введите число от 0 до 5.");
                    break;
            }
        }
    }
}
