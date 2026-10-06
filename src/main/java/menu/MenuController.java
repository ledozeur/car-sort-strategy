package menu;

import collection.CarArray;

import java.util.Scanner;

public class MenuController {

private final CarArray ca = new CarArray();
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(
                    "1 — заполнить массив\n" +
                            "2 — показать\n" +
                            "3 — сортировать\n" +
                            "4 — записать в файл\n" +
                            "5 — подсчитать N\n" +
                            "0 — выход (break)");

            if (!sc.hasNextInt()) {
                sc.nextLine();
                System.out.println("Введите число от 0 до 5.");
                continue;
            }
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 0:
                    return;
            }

        }

    }
}