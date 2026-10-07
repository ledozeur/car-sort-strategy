package validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarValidatorTest {

    @Test
    void blankModelIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> CarValidator.modelValidation(""));
        assertThrows(IllegalArgumentException.class, () -> CarValidator.modelValidation("   "));
    }

    @Test
    void modelLengthBoundary() {
        String ok = "a".repeat(60);
        assertEquals(ok, CarValidator.modelValidation(ok));
        assertThrows(IllegalArgumentException.class,
                () -> CarValidator.modelValidation("a".repeat(61)));
    }

    @Test
    void powerHpBoundaries() {
        assertEquals(30, CarValidator.powerHpValidation(30));
        assertEquals(1200, CarValidator.powerHpValidation(1200));
        assertThrows(IllegalArgumentException.class, () -> CarValidator.powerHpValidation(29));
        assertThrows(IllegalArgumentException.class, () -> CarValidator.powerHpValidation(1201));
    }

    @Test
    void productionYearBoundaries() {
        assertEquals(1900, CarValidator.productionYearValidation(1900));
        assertEquals(2026, CarValidator.productionYearValidation(2026));
        assertThrows(IllegalArgumentException.class, () -> CarValidator.productionYearValidation(1899));
        assertThrows(IllegalArgumentException.class, () -> CarValidator.productionYearValidation(2027));
    }
}