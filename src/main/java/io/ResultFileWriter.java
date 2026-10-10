package io;

import collection.CarArray;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ResultFileWriter implements ResultWriter {
    private final String defaultPath = "data/output/";

    @Override
    public boolean save(CarArray data, String path) {
        String txt = carArrayToTxt(data);

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(
                path + Instant.now().toEpochMilli()
                        + ".txt"))) {

            bf.write(txt);
        } catch (IOException e) {
            System.out.println("Error when save array:" + e);
            return false;
        }
        return true;
    }

    @Override
    public boolean save(CarArray data) {
        return save(data, defaultPath);
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
