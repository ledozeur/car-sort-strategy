package fill;

import collection.CarArray;
import model.Car;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomCarFillService implements CarFillService {
    private final RandomGenerator generator = new Random();
    private final String fileWithCarNamePath;
    private final Integer minProductionYear;
    private final Integer maxProductionYear;
    private final Integer minPowerHp;
    private final Integer maxPowerHp;

    public RandomCarFillService(Integer minProductionYear,
                                Integer maxProductionYear,
                                String fileWithCarNamePath,
                                Integer minPowerHp,
                                Integer maxPowerHp) {
        this.minProductionYear = minProductionYear;
        this.maxProductionYear = maxProductionYear;
        this.fileWithCarNamePath = fileWithCarNamePath;
        this.minPowerHp = minPowerHp;
        this.maxPowerHp = maxPowerHp;
    }

    public RandomCarFillService() {
        fileWithCarNamePath = "data/cars.txt";
        minPowerHp = 30;
        maxPowerHp = 1200;
        minProductionYear = 1900;
        maxProductionYear = 2026;
    }


    @Override
    public CarArray create(Integer limit) {
        try (Stream<String> lines = Files.lines(Path.of(fileWithCarNamePath))) {
            CarArray cars = new CarArray();

            List<String> carNames = lines
                    .limit((long) (limit * 1.5))
                    .map(line -> line.split(";")[0])
                    .toList();

            if (carNames.isEmpty()) {
                throw new IllegalStateException("File is empty");
            }

            IntStream.range(0, limit)
                    .mapToObj(i -> carNames.get(
                            generator.nextInt(carNames.size())
                    ))
                    .map(this::generateCar)
                    .forEach(cars::add);

            return cars;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Car generateCar(String carName) {
        return new Car.CarBuilder(carName)
                .productionYear(generator.nextInt(minProductionYear, maxProductionYear + 1))
                .powerHp(generator.nextInt(minPowerHp, maxPowerHp + 1))
                .build();
    }
}
