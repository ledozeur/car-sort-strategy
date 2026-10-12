package parser;

import model.Car;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CarParserTest {

    private final CarParser parser = new CarParser();

    private void assertRejected(String line) {
        Optional<Car> result = parser.parse(line);
        assertTrue(result.isEmpty(), "Line must be rejected: \"" + line + "\"");
    }

    // --- valid lines ---

    @Test
    void parsesValidLine() {
        Optional<Car> result = parser.parse("Camry;249;2021");

        assertTrue(result.isPresent());
        assertEquals("Camry", result.get().getModel());
        assertEquals(249, result.get().getPowerHp());
        assertEquals(2021, result.get().getProductionYear());
    }

    @Test
    void parsesModelWithSpaces() {
        Optional<Car> result = parser.parse("Land Cruiser 200;309;2015");

        assertTrue(result.isPresent());
        assertEquals("Land Cruiser 200", result.get().getModel());
    }

    @Test
    void trimsWhitespaceAroundFields() {
        Optional<Car> result = parser.parse("  Camry ; 249 ; 2021  ");

        assertTrue(result.isPresent());
        assertEquals("Camry", result.get().getModel());
        assertEquals(249, result.get().getPowerHp());
        assertEquals(2021, result.get().getProductionYear());
    }

    @Test
    void acceptsBoundaryValues() {
        assertTrue(parser.parse("X;30;1900").isPresent());
        assertTrue(parser.parse("X;1200;2026").isPresent());
    }

    // --- wrong number of fields ---

    @Test
    void rejectsTooFewFields() {
        assertRejected("Rapid;90");
        assertRejected("Rapid");
        assertRejected("");
    }

    @Test
    void rejectsTooManyFields() {
        assertRejected("Logan;82;2013;extra");
    }

    @Test
    void rejectsEmptyLastField() {
        assertRejected("Creta;149;");
    }

    @Test
    void rejectsExtraTrailingSeparators() {
        // split(";") silently drops trailing empty strings, so this line
        // looks like exactly 3 fields to the parser
        assertRejected("Logan;82;2013;;");
    }

    // --- numbers that are not numbers ---

    @Test
    void rejectsNonNumericPower() {
        assertRejected("Passat;abc;2016");
    }

    @Test
    void rejectsNonNumericYear() {
        assertRejected("Octavia;110;two thousand");
    }

    @Test
    void rejectsEmptyMiddleField() {
        assertRejected("Solaris;;2018");
    }

    // --- values rejected by the validator ---

    @Test
    void rejectsBlankModel() {
        assertRejected(";150;2017");
        assertRejected("   ;150;2017");
    }

    @Test
    void rejectsTooLongModel() {
        assertRejected("a".repeat(61) + ";100;2000");
    }

    @Test
    void rejectsPowerOutOfRange() {
        assertRejected("Corolla;29;2019");
        assertRejected("Civic;1201;2018");
    }

    @Test
    void rejectsYearOutOfRange() {
        assertRejected("Accord;192;1899");
        assertRejected("Mustang;450;2027");
    }

    @Test
    void rejectedLineDoesNotAffectNextParse() {
        assertFalse(parser.parse("Rapid;90").isPresent());
        assertTrue(parser.parse("Rio;100;2012").isPresent());
    }
}