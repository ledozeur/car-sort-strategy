package fill;

import collection.CarArray;
import model.Car;
import parser.CarParser;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class FileCarFillService implements CarFillService{
    private final Path path;
    private final CarParser parser;

    public FileCarFillService(Path path, CarParser parser) {
        this.path = path;
        this.parser = parser;
    }

    @Override
    public CarArray create(Integer limit) {
        CarArray cars = new CarArray();
        try (Stream<String> lines = Files.lines(path)) {
            Stream<Car> carStream = lines
                    .map(parser::parse)
                    .flatMap(Optional::stream);
            if (limit != -1) {
                carStream = carStream.limit(limit);
            }
            carStream.forEach(cars::add);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return cars;
    }
}
