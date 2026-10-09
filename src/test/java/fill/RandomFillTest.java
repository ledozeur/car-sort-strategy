package fill;

import collection.CarArray;
import model.Car;
import org.junit.jupiter.api.Test;
import validation.CarValidator;

import static org.junit.jupiter.api.Assertions.*;

public class RandomFillTest {
    private final RandomCarFillService fillService = new RandomCarFillService();

    @Test
    public void generationTest() {
        CarArray array = fillService.create(10);
        assertTrue(array.size() != 0, "Array is empty");
    }

    @Test
    public void arrayUniqueTest(){
        CarArray array = fillService.create(10);
        CarArray array2 = fillService.create(10);
        assertNotEquals(array, array2, "Arrays not unique");
    }

    @Test
    public void limitTest(){
        Integer limit = 10;
        CarArray array = fillService.create(limit);
        assertEquals(limit, array.size());
    }

    @Test
    public void objectValidationTest(){
        CarArray array = fillService.create(10);
        for (Car car : array) {
            assertDoesNotThrow(() -> CarValidator.modelValidation(car.getModel()));
            assertDoesNotThrow(() -> CarValidator.productionYearValidation(car.getProductionYear()));
            assertDoesNotThrow(() -> CarValidator.powerHpValidation(car.getPowerHp()));
        }
    }

}
