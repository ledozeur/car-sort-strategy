package fill;

import collection.CarArray;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManualCarFillServiceTest {
    @Test
    void create_shouldReturnAllEnteredCars() {
        Scanner scanner = new Scanner(
                "Toyota Camry;200;2022\n" +
                        "BMW M5;625;2024\n" +
                        "-1\n"
        );

        ManualCarFillService service =
                new ManualCarFillService(scanner);

        CarArray result = service.create(10);

        assertEquals(2, result.size());

        assertEquals("Toyota Camry", result.get(0).getModel());
        assertEquals(200, result.get(0).getPowerHp());
        assertEquals(2022, result.get(0).getProductionYear());

        assertEquals("BMW M5", result.get(1).getModel());
        assertEquals(625, result.get(1).getPowerHp());
        assertEquals(2024, result.get(1).getProductionYear());
    }

    @Test
    void create_shouldReturnEmptyArrayWhenImmediatelyStopped() {
        Scanner scanner = new Scanner("-1\n");
        ManualCarFillService service =
                new ManualCarFillService(scanner);

        CarArray result = service.create(10);

        assertEquals(0, result.size());
    }

    @Test
    void create_shouldIgnoreInvalidInput() {
        Scanner scanner = new Scanner(
                "invalid input\n" +
                        "Toyota Camry;200;2022\n" +
                        "-1\n"
        );

        ManualCarFillService service =
                new ManualCarFillService(scanner);

        CarArray result = service.create(10);

        assertEquals(1, result.size());
        assertEquals("Toyota Camry", result.get(0).getModel());
    }

    @Test
    void create_shouldStopAfterLimitCars() {
        Scanner scanner = new Scanner(
                "Toyota Camry;200;2022\n" +
                        "BMW M5;625;2024\n" +
                        "Audi A6;245;2023\n" +
                        "Mercedes-Benz E-Class;250;2022\n" +
                        "-1\n"
        );

        ManualCarFillService service =
                new ManualCarFillService(scanner);

        CarArray result = service.create(2);

        assertEquals(2, result.size());
    }

    @Test
    void create_shouldAllowUnlimitedCarsWhenLimitIsMinusOne() {
        Scanner scanner = new Scanner(
                "Toyota Camry;200;2022\n" +
                        "BMW M5;625;2024\n" +
                        "Audi A6;245;2023\n" +
                        "-1\n"
        );

        ManualCarFillService service =
                new ManualCarFillService(scanner);

        CarArray result = service.create(-1);

        assertEquals(3, result.size());
    }
}
