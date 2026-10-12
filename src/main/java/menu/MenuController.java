package menu;

import collection.CarArray;
import comparator.CarComparator;
import comparator.CarModelComparator;
import comparator.CarPowerHpComparator;
import comparator.CarProductionYearComparator;
import fill.FileCarFillService;
import fill.ManualCarFillService;
import fill.RandomCarFillService;
import concurrency.ParallelCountService;
import io.ResultFileWriter;
import model.Car;
import parser.CarParser;
import strategy.BubbleSortStrategy;
import strategy.EvenOddSortDecorator;
import strategy.InsertionSortStrategy;
import strategy.SortStrategy;

import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class MenuController {

    private CarArray cars = new CarArray();
    private CarArray lastSorted;

    private final BubbleSortStrategy bubbleSortStrategy = new BubbleSortStrategy();
    private final InsertionSortStrategy insertionSortStrategy = new InsertionSortStrategy();

    private final CarModelComparator carModelComparator = new CarModelComparator();
    private final CarPowerHpComparator carPowerHpComparator = new CarPowerHpComparator();
    private final CarProductionYearComparator carProductionYearComparator = new CarProductionYearComparator();
    private final ResultFileWriter resultFileWriter = new ResultFileWriter();
    private final ParallelCountService parallelCountService = new ParallelCountService();
    private final CarParser carParser = new CarParser();

    private static final int COUNT_THREAD_COUNT = 4;

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
                                System.out.println("Загружено автомобилей: " + cars.size());
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
                        System.out.println(cars);
                    } else {
                        System.out.println("Массив пуст.");
                    }

                    break;
                case 3: {
                    if (cars.size() == 0) {
                        System.out.println("Массив пуст. Сначала заполните данные (пункт 1).");
                        break;
                    }

                    System.out.println(
                            "\nАлгоритм:\n" +
                                    "1 — Bubble Sort\n" +
                                    "2 — Insertion Sort\n" +
                                    "3 — Bubble Sort + Even/Odd (чётные по полю сортируются, нечётные на местах)\n" +
                                    "4 — Insertion Sort + Even/Odd (чётные по полю сортируются, нечётные на местах)\n" +
                                    "0 — назад");

                    if (!sc.hasNextInt()) {
                        sc.nextLine();
                        System.out.println("Введите число от 0 до 4.");
                        break;
                    }
                    int alg = sc.nextInt();
                    sc.nextLine();

                    if (alg == 0) {
                        break;
                    }
                    if (alg < 1 || alg > 4) {
                        System.out.println("Неверный алгоритм.");
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

                    if (field == 0) {
                        break;
                    }

                    CarComparator comparator;
                    if (field == 1) {
                        comparator = carModelComparator;
                    } else if (field == 2) {
                        comparator = carPowerHpComparator;
                    } else if (field == 3) {
                        comparator = carProductionYearComparator;
                    } else {
                        System.out.println("Неверное поле.");
                        break;
                    }

                    boolean evenOddMode = alg == 3 || alg == 4;
                    if (evenOddMode && field == 1) {
                        System.out.println(
                                "Even/Odd для модели не поддерживается. Выберите мощность (2) или год (3).");
                        break;
                    }

                    SortStrategy strategy;
                    if (alg == 1) {
                        strategy = bubbleSortStrategy;
                    } else if (alg == 2) {
                        strategy = insertionSortStrategy;
                    } else if (evenOddMode) {
                        Function<Car, Integer> evenOddKey = field == 3
                                ? Car::getProductionYear
                                : Car::getPowerHp;
                        SortStrategy baseSort = alg == 3 ? bubbleSortStrategy : insertionSortStrategy;
                        strategy = new EvenOddSortDecorator(baseSort, evenOddKey);
                    } else {
                        break;
                    }

                    lastSorted = strategy.sort(cars, comparator);

                    System.out.println("Отсортированный массив:");
                    System.out.println(lastSorted);
                    break;
                }
                case 4: {
                    if (lastSorted == null || lastSorted.size() == 0) {
                        System.out.println("Нет результата сортировки. Сначала выполните пункт 3.");
                        break;
                    }
                    if (resultFileWriter.save(lastSorted)) {
                        System.out.println("Данные добавлены в файл output/sorted_cars.txt");
                    } else {
                        System.out.println("Не удалось записать файл.");
                    }
                    break;
                }
                case 5: {
                    if (cars.size() == 0) {
                        System.out.println("Массив пуст. Сначала заполните данные (пункт 1).");
                        break;
                    }
                    System.out.println("Введите автомобиль N (модель;мощность;год): ");
                    String carLine = sc.nextLine().trim();
                    Optional<Car> targetCar = carParser.parse(carLine);
                    if (targetCar.isEmpty()) {
                        System.out.println("Некорректные данные. Формат: модель;мощность;год");
                        break;
                    }
                    parallelCountService.countAndPrint(cars, targetCar.get(), COUNT_THREAD_COUNT);
                    break;
                }
                case 0:
                    return;
                default:
                    System.out.println("Нет такого пункта. Введите число от 0 до 5.");
                    break;
            }
        }
    }
}
