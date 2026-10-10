package utility;

import collection.CarArray;

import java.util.stream.IntStream;

public class CopyCarArray {
    public static CarArray createCopy(CarArray old) {
        CarArray copy = new CarArray();
        IntStream.range(0, old.size())
                .mapToObj(old::get)
                .forEach(copy::add);
        return copy;
    }
}
