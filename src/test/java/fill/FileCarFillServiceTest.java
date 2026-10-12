package fill;

import collection.CarArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import parser.CarParser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileCarFillServiceTest {

    @TempDir
    Path tempDir;

    private final CarParser parser = new CarParser();

    private Path writeFile(String... lines) throws IOException {
        return Files.write(tempDir.resolve("cars.txt"), List.of(lines));
    }

    private FileCarFillService serviceFor(Path path) {
        return new FileCarFillService(path, parser);
    }

    // --- reading ---

    @Test
    void readsAllValidLinesInOrder() throws IOException {
        Path file = writeFile(
                "Camry;249;2021",
                "Corolla;139;2019",
                "Civic;158;2018"
        );

        CarArray result = serviceFor(file).create(-1);

        assertEquals(3, result.size());
        assertEquals("Camry", result.get(0).getModel());
        assertEquals("Corolla", result.get(1).getModel());
        assertEquals("Civic", result.get(2).getModel());
        assertEquals(139, result.get(1).getPowerHp());
        assertEquals(2018, result.get(2).getProductionYear());
    }

    @Test
    void skipsInvalidLinesAndKeepsValidOnes() throws IOException {
        Path file = writeFile(
                "Camry;249;2021",
                "Rapid;90",
                "Passat;abc;2016",
                "Corolla;29;2019",
                "Rio;100;2012"
        );

        CarArray result = serviceFor(file).create(-1);

        assertEquals(2, result.size());
        assertEquals("Camry", result.get(0).getModel());
        assertEquals("Rio", result.get(1).getModel());
    }

    @Test
    void keepsDuplicateLines() throws IOException {
        Path file = writeFile(
                "Camry;249;2021",
                "Camry;249;2021"
        );

        CarArray result = serviceFor(file).create(-1);

        assertEquals(2, result.size());
        assertEquals(result.get(0), result.get(1));
    }

    @Test
    void emptyFileGivesEmptyArray() throws IOException {
        Path file = writeFile();

        assertEquals(0, serviceFor(file).create(-1).size());
    }

    @Test
    void fileWithOnlyInvalidLinesGivesEmptyArray() throws IOException {
        Path file = writeFile(
                "Rapid;90",
                ";150;2017",
                "Logan;82;2013;extra"
        );

        assertEquals(0, serviceFor(file).create(-1).size());
    }

    // --- limit ---

    @Test
    void limitStopsAtRequestedNumberOfCars() throws IOException {
        Path file = writeFile(
                "A;100;2000",
                "B;100;2000",
                "C;100;2000",
                "D;100;2000"
        );

        CarArray result = serviceFor(file).create(2);

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getModel());
        assertEquals("B", result.get(1).getModel());
    }

    @Test
    void limitCountsOnlyValidCars() throws IOException {
        // the first three lines are garbage: if limit were applied before
        // filtering, they would eat the whole limit and the result would be empty
        Path file = writeFile(
                "Rapid;90",
                "Passat;abc;2016",
                "Corolla;29;2019",
                "A;100;2000",
                "B;100;2000",
                "C;100;2000"
        );

        CarArray result = serviceFor(file).create(2);

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getModel());
        assertEquals("B", result.get(1).getModel());
    }

    @Test
    void limitLargerThanAvailableReturnsEverything() throws IOException {
        Path file = writeFile(
                "A;100;2000",
                "B;100;2000"
        );

        assertEquals(2, serviceFor(file).create(100).size());
    }

    @Test
    void limitEqualToAvailableReturnsEverything() throws IOException {
        Path file = writeFile(
                "A;100;2000",
                "B;100;2000"
        );

        assertEquals(2, serviceFor(file).create(2).size());
    }

    @Test
    void minusOneMeansNoLimit() throws IOException {
        String[] lines = new String[50];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = "Model" + i + ";100;2000";
        }
        Path file = writeFile(lines);

        // 50 > default capacity of CarArray (32), so this also covers growth
        assertEquals(50, serviceFor(file).create(-1).size());
    }

    // --- errors ---

    @Test
    void missingFileThrowsUncheckedIoException() {
        Path missing = tempDir.resolve("does-not-exist.txt");

        assertThrows(UncheckedIOException.class, () -> serviceFor(missing).create(-1));
    }

    // --- independence of calls ---

    @Test
    void eachCallReturnsNewIndependentArray() throws IOException {
        Path file = writeFile("A;100;2000");
        FileCarFillService service = serviceFor(file);

        CarArray first = service.create(-1);
        CarArray second = service.create(-1);

        first.clear();

        assertEquals(0, first.size());
        assertEquals(1, second.size());
    }
}