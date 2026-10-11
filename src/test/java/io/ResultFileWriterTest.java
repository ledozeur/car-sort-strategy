package io;

import collection.CarArray;
import model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ResultFileWriterTest {

    @TempDir
    Path tempDir;

    private final ResultFileWriter writer = new ResultFileWriter();

    @Test
    void save_shouldWriteAllCarsToFile() throws IOException {
        CarArray cars = new CarArray();
        cars.add(new Car.CarBuilder("Toyota Camry")
                .powerHp(200)
                .productionYear(2022)
                .build());
        cars.add(new Car.CarBuilder("BMW M5")
                .powerHp(625)
                .productionYear(2024)
                .build());

        String path = tempDir.toString() + java.io.File.separator;

        boolean result = writer.save(cars, path);

        assertTrue(result);

        Path outputFile = findOutputFile();
        String content = Files.readString(outputFile);

        assertEquals(
                "Toyota Camry;200;2022\n" +
                        "BMW M5;625;2024\n",
                content
        );
    }

    @Test
    void save_shouldWriteEmptyFileWhenArrayIsEmpty() throws IOException {
        CarArray cars = new CarArray();
        String path = tempDir.toString() + java.io.File.separator;

        boolean result = writer.save(cars, path);

        assertTrue(result);

        Path outputFile = findOutputFile();

        assertEquals("", Files.readString(outputFile));
    }

    @Test
    void save_shouldReturnFalseWhenDirectoryDoesNotExist() {
        CarArray cars = new CarArray();
        cars.add(new Car.CarBuilder("Toyota Camry")
                .powerHp(200)
                .productionYear(2022)
                .build());

        String path = tempDir.resolve("nonexistent")
                .toString() + java.io.File.separator;

        boolean result = writer.save(cars, path);

        assertFalse(result);
    }

    @Test
    void save_shouldPreserveSpecialCharactersInModel() throws IOException {
        CarArray cars = new CarArray();
        cars.add(new Car.CarBuilder("Mercedes-Benz E-Class")
                .powerHp(250)
                .productionYear(2023)
                .build());

        String path = tempDir.toString() + java.io.File.separator;

        boolean result = writer.save(cars, path);

        assertTrue(result);

        assertEquals(
                "Mercedes-Benz E-Class;250;2023\n",
                Files.readString(findOutputFile())
        );
    }

    private Path findOutputFile() throws IOException {
        try (var files = Files.list(tempDir)) {
            List<Path> outputFiles = files
                    .filter(path -> path.toString().endsWith(".txt"))
                    .toList();

            assertEquals(1, outputFiles.size());

            return outputFiles.get(0);
        }
    }
}
