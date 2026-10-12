package io;

import collection.CarArray;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResultFileWriter implements ResultWriter {
    private final String defaultPath;

    public ResultFileWriter() {
        defaultPath = "data/output/";
    }

    public ResultFileWriter(String path) {
        defaultPath = path;
    }

    @Override
    public boolean save(CarArray data) {
        String txt = carArrayToTxt(data);

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(
                defaultPath + Instant.now().toEpochMilli()
                        + ".txt"))) {

            bf.write(txt);
        } catch (IOException e) {
            System.out.println("Error when save array:" + e);
            return false;
        }
        return true;
    }

    private String carArrayToTxt(CarArray array) {
        return IntStream.range(0, array.size())
                .mapToObj(array::get)
                .map(car -> car.getModel() + ";"
                        + car.getPowerHp() + ";"
                        + car.getProductionYear() + "\n")
                .collect(Collectors.joining());
    }
}
